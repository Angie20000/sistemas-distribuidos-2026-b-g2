package co.edu.corhuila.property.booking.expiry;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Minimal booking-service: POST /api/v1/reservas/{id}/expirar (E-19) and GET /metrics. */
public final class ExpiryServer {

    /** estado of each Reserva; the real service reads booking.reservation. */
    final Map<String, String> estados = new ConcurrentHashMap<>();
    final ExpiryMetrics metrics = new ExpiryMetrics();
    final OutboxRelay relay;
    private final HttpServer http;

    public ExpiryServer(int port, OutboxRelay.Publisher publisher) throws IOException {
        relay = new OutboxRelay(publisher);
        http = HttpServer.create(new InetSocketAddress(port), 0);
        http.createContext("/api/v1/reservas/", new TracingHandler(this::expirar));
        http.createContext("/metrics", ex -> reply(ex, 200, metrics.scrape()));
    }

    private void expirar(HttpExchange ex) throws IOException {
        long start = System.nanoTime();
        String[] path = ex.getRequestURI().getPath().split("/"); // ["", api, v1, reservas, {id}, expirar]
        String id = path.length == 6 && path[5].equals("expirar") ? path[4] : null;
        String estado = id == null ? null : estados.get(id);
        ExpiryMetrics.Result result;
        int status;
        String body;
        if (estado == null) {
            result = ExpiryMetrics.Result.NOT_FOUND;
            status = 404;
            body = "{\"error\":\"NOT_FOUND\"}";
        } else if (estado.equals("PENDIENTE") || estado.equals("EXPIRADA")) {
            result = estado.equals("EXPIRADA") ? ExpiryMetrics.Result.ALREADY_EXPIRED : ExpiryMetrics.Result.EXPIRED;
            if (result == ExpiryMetrics.Result.EXPIRED) {
                estados.put(id, "EXPIRADA");
                relay.save("ReservaExpirada", id);
                LogLine.info("reserva.expired").msg("reservation moved to EXPIRADA")
                        .with("reserva_id", id).with("motivo_cierre", "TIMEOUT_PAGO_EXCEDIDO").print();
            }
            status = 200;
            body = "{\"id\":\"" + id + "\",\"estado\":\"EXPIRADA\"}";
        } else {
            result = ExpiryMetrics.Result.INVALID_TRANSITION;
            LogLine.warn("reserva.expire.rejected").msg("reservation is " + estado + ", cannot expire")
                    .with("reserva_id", id).with("estado_actual", estado)
                    .with("error_code", "INVALID_STATUS_TRANSITION").print();
            status = 422;
            body = "{\"error\":\"INVALID_STATUS_TRANSITION\"}";
        }
        metrics.observe(result, (System.nanoTime() - start) / 1e9);
        reply(ex, status, body);
    }

    private static void reply(HttpExchange ex, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(status, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }

    public void start() { http.start(); }
    public void stop() { http.stop(0); }
    public int port() { return http.getAddress().getPort(); }

    public static void main(String[] args) throws IOException {
        ExpiryServer server = new ExpiryServer(3001,
                (key, headers, body) -> System.out.println("AMQP " + key + " " + headers + " " + body));
        server.estados.put("3c8e2a71-5d4f-4b9a-8e6c-0a1b2c3d4e5f", "PENDIENTE");
        server.start();
        System.out.println("booking-service (expiry) listening on :3001");
    }
}

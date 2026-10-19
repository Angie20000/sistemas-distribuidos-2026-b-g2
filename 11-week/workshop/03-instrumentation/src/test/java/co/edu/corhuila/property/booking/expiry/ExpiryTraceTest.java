package co.edu.corhuila.property.booking.expiry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExpiryTraceTest {

    private static final String WORKER_TRACE = "9f3a1c7e5b2d4086a1e2f3b4c5d6e7f8";
    private static final String WORKER_SPAN = "2c3d4e5f60718293";

    private final List<Map<String, String>> publishedHeaders = new ArrayList<>();
    private final HttpClient client = HttpClient.newHttpClient();
    private ExpiryServer booking;

    @BeforeEach
    void up() throws Exception {
        booking = new ExpiryServer(0, (key, headers, body) -> publishedHeaders.add(headers));
        booking.estados.put("r-pendiente", "PENDIENTE");
        booking.estados.put("r-confirmada", "CONFIRMADA");
        booking.start();
    }

    @AfterEach
    void down() {
        booking.stop();
    }

    private HttpResponse<String> expirar(String id) throws Exception {
        return client.send(HttpRequest.newBuilder(
                        URI.create("http://localhost:" + booking.port() + "/api/v1/reservas/" + id + "/expirar"))
                .header("traceparent", "00-" + WORKER_TRACE + "-" + WORKER_SPAN + "-01")
                .POST(HttpRequest.BodyPublishers.noBody()).build(), HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void workerTraceIdReachesTheReservaExpiradaMessage() throws Exception {
        HttpResponse<String> response = expirar("r-pendiente");
        assertEquals(200, response.statusCode());
        assertEquals(1, booking.relay.publishPending());

        String sent = publishedHeaders.get(0).get("traceparent");
        assertEquals(WORKER_TRACE, sent.split("-")[1], "the message must keep the worker's trace_id");
        assertNotEquals(WORKER_SPAN, sent.split("-")[2]);
        assertEquals(WORKER_TRACE, response.headers().firstValue("traceparent").orElseThrow().split("-")[1]);
    }

    @Test
    void confirmedReservaIsRejectedAndCounted() throws Exception {
        assertEquals(422, expirar("r-confirmada").statusCode());
        assertEquals(0, booking.relay.publishPending());
        String metrics = booking.metrics.scrape();
        assertTrue(metrics.contains("booking_expirations_total{result=\"invalid_transition\"} 1"));
        assertTrue(metrics.contains("booking_expire_duration_seconds_count 1"));
    }
}

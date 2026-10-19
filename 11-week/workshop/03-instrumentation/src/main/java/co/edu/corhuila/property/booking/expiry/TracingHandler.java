package co.edu.corhuila.property.booking.expiry;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

/**
 * Wraps a handler of booking-service: continues the trace sent by property-worker (or
 * starts one), answers it in the response and logs the request with its duration.
 */
public final class TracingHandler implements HttpHandler {

    private final HttpHandler next;

    public TracingHandler(HttpHandler next) {
        this.next = next;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        long startNanos = System.nanoTime();
        Traceparent trace = Traceparent.continueOrStart(exchange.getRequestHeaders().getFirst("traceparent"));
        String correlation = exchange.getRequestHeaders().getFirst("X-Correlation-Id");
        RequestScope.open(trace, correlation == null ? "corr-" + trace.traceId.substring(0, 8) : correlation);
        exchange.getResponseHeaders().add("traceparent", trace.header());
        try {
            next.handle(exchange);
        } finally {
            LogLine.info("http.request.completed")
                    .msg(exchange.getRequestMethod() + " " + exchange.getRequestURI().getPath())
                    .with("status", exchange.getResponseCode())
                    .with("duration_ms", (System.nanoTime() - startNanos) / 1_000_000)
                    .print();
            RequestScope.close();
        }
    }
}

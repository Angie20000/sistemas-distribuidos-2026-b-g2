package co.edu.corhuila.property.booking.expiry;

/** What one request to booking-service carries while it is being handled. */
public final class RequestScope {

    private static final ThreadLocal<Traceparent> TRACE = new ThreadLocal<>();
    private static final ThreadLocal<String> CORRELATION = new ThreadLocal<>();

    private RequestScope() { }

    static void open(Traceparent trace, String correlationId) {
        TRACE.set(trace);
        CORRELATION.set(correlationId);
    }

    static void close() {
        TRACE.remove();
        CORRELATION.remove();
    }

    public static Traceparent trace() { return TRACE.get(); }
    public static String correlationId() { return CORRELATION.get(); }
}

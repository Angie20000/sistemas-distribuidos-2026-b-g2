package co.edu.corhuila.property.booking.expiry;

import java.time.Instant;
import java.util.List;

/** Builder for one line of the CP2 log contract. Usage: LogLine.warn("x").msg("y").with("k", v).print(); */
public final class LogLine {

    private static final String SERVICE = System.getenv().getOrDefault("SERVICE_NAME", "booking-service");
    private static final String VERSION = System.getenv().getOrDefault("SERVICE_VERSION", "2.0.0");
    private static final String ENV = System.getenv().getOrDefault("APP_ENV", "dev");
    private static final List<String> BLOCKED = List.of("authorization", "serviceToken", "usuarioId", "email");

    private final StringBuilder json = new StringBuilder();

    private LogLine(String level, String event) {
        Traceparent trace = RequestScope.trace();
        add("timestamp", Instant.now().toString());
        add("level", level);
        add("service", SERVICE);
        add("version", VERSION);
        add("environment", ENV);
        add("trace_id", trace == null ? null : trace.traceId);
        add("span_id", trace == null ? null : trace.parentId);
        add("correlation_id", RequestScope.correlationId());
        add("event", event);
    }

    public static LogLine info(String event) { return new LogLine("INFO", event); }
    public static LogLine warn(String event) { return new LogLine("WARN", event); }
    public static LogLine error(String event) { return new LogLine("ERROR", event); }

    public LogLine msg(String message) { return add("message", message); }

    public LogLine with(String key, Object value) {
        return BLOCKED.contains(key) ? this : add(key, value);
    }

    public void print() {
        System.out.println("{" + json + "}");
    }

    private LogLine add(String key, Object value) {
        if (json.length() > 0) json.append(',');
        json.append('"').append(key).append("\":");
        if (value == null || value instanceof Number) json.append(value);
        else json.append('"').append(String.valueOf(value).replace("\"", "'")).append('"');
        return this;
    }
}

package co.edu.corhuila.property.booking.expiry;

import java.util.concurrent.ThreadLocalRandom;

/** W3C traceparent header of booking-service: version-traceId-parentId-flags. */
public final class Traceparent {

    public final String traceId;
    public final String parentId;

    private Traceparent(String traceId, String parentId) {
        this.traceId = traceId;
        this.parentId = parentId;
    }

    /** Keeps the trace of property-worker when the header is valid; otherwise a new trace. */
    public static Traceparent continueOrStart(String header) {
        if (header != null) {
            String[] parts = header.trim().split("-");
            if (parts.length == 4 && parts[0].equals("00")
                    && parts[1].matches("[0-9a-f]{32}") && parts[2].matches("[0-9a-f]{16}")) {
                return new Traceparent(parts[1], hex(16));
            }
        }
        return new Traceparent(hex(32), hex(16));
    }

    /** Same trace, new span: used for the message that leaves through the outbox. */
    public Traceparent nextSpan() {
        return new Traceparent(traceId, hex(16));
    }

    public String header() {
        return "00-" + traceId + "-" + parentId + "-01";
    }

    private static String hex(int length) {
        StringBuilder sb = new StringBuilder(length);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) sb.append(Character.forDigit(random.nextInt(16), 16));
        return sb.toString();
    }
}

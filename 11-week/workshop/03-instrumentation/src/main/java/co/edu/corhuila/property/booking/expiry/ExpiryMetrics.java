package co.edu.corhuila.property.booking.expiry;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/** booking_expirations_total by result and booking_expire_duration_seconds (histogram). */
public final class ExpiryMetrics {

    public enum Result { EXPIRED, ALREADY_EXPIRED, INVALID_TRANSITION, NOT_FOUND }

    /** Seconds. expirar is one UPDATE plus one INSERT in a transaction: tens of ms. */
    private static final double[] LE = {0.01, 0.025, 0.05, 0.1, 0.25, 0.5, 1};

    private final Map<Result, LongAdder> byResult = new EnumMap<>(Result.class);
    private final LongAdder[] buckets = new LongAdder[LE.length + 1];
    private double sumSeconds;

    public ExpiryMetrics() {
        for (Result r : Result.values()) byResult.put(r, new LongAdder());
        for (int i = 0; i < buckets.length; i++) buckets[i] = new LongAdder();
    }

    public synchronized void observe(Result result, double seconds) {
        byResult.get(result).increment();
        for (int i = 0; i < LE.length; i++) if (seconds <= LE[i]) buckets[i].increment();
        buckets[LE.length].increment();
        sumSeconds += seconds;
    }

    public synchronized String scrape() {
        StringBuilder out = new StringBuilder("# TYPE booking_expirations_total counter\n");
        byResult.forEach((r, n) -> out.append("booking_expirations_total{result=\"")
                .append(r.name().toLowerCase()).append("\"} ").append(n.sum()).append('\n'));
        out.append("# TYPE booking_expire_duration_seconds histogram\n");
        for (int i = 0; i < LE.length; i++) {
            out.append("booking_expire_duration_seconds_bucket{le=\"").append(LE[i]).append("\"} ")
                    .append(buckets[i].sum()).append('\n');
        }
        long total = buckets[LE.length].sum();
        out.append("booking_expire_duration_seconds_bucket{le=\"+Inf\"} ").append(total).append('\n')
                .append("booking_expire_duration_seconds_sum ").append(sumSeconds).append('\n')
                .append("booking_expire_duration_seconds_count ").append(total).append('\n');
        return out.toString();
    }
}

package co.edu.corhuila.property.booking.expiry;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The async boundary: rows of booking.outbox_event keep the traceparent of the request
 * that wrote them, and the relay copies it into the AMQP headers when it publishes.
 */
public final class OutboxRelay {

    /** One row of booking.outbox_event. */
    public record OutboxRow(String eventType, String reservaId, String traceparent) { }

    /** RabbitMQ in the real service; an in-memory list in the test. */
    public interface Publisher {
        void publish(String routingKey, Map<String, String> headers, String body);
    }

    private final ConcurrentLinkedQueue<OutboxRow> pending = new ConcurrentLinkedQueue<>();
    private final Publisher publisher;

    public OutboxRelay(Publisher publisher) {
        this.publisher = publisher;
    }

    /** Called inside the same transaction that moves the Reserva to EXPIRADA. */
    public void save(String eventType, String reservaId) {
        pending.add(new OutboxRow(eventType, reservaId, RequestScope.trace().nextSpan().header()));
    }

    /** Runs later, outside the request: the trace comes from the row, not from a ThreadLocal. */
    public int publishPending() {
        int sent = 0;
        for (OutboxRow row; (row = pending.poll()) != null; sent++) {
            RequestScope.open(Traceparent.continueOrStart(row.traceparent()), null);
            publisher.publish("reservas." + row.eventType(),
                    Map.of("traceparent", row.traceparent(), "event_type", row.eventType()),
                    "{\"reservaId\":\"" + row.reservaId() + "\"}");
            LogLine.info("outbox.event.published").msg(row.eventType() + " published")
                    .with("reserva_id", row.reservaId()).with("traceparent", row.traceparent()).print();
            RequestScope.close();
        }
        return sent;
    }
}

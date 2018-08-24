package io.zeebe.camel.fn;

import io.zeebe.camel.api.event.JobEvent;
import io.zeebe.camel.api.event.MessageHeader;
import io.zeebe.client.api.events.JobState;
import io.zeebe.client.api.record.JobRecord;
import io.zeebe.client.api.record.Record;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.camel.Exchange;

public class CreateExchangeForEvent implements Function<Record, Exchange> {

    private final Supplier<Exchange> exchangeSupplier;

    public CreateExchangeForEvent(final Supplier<Exchange> exchangeSupplier) {
        this.exchangeSupplier = exchangeSupplier;
    }

    @Override
    public Exchange apply(final Record record) {
        MessageHeader header = MessageHeader.from(record.getMetadata())
        final Exchange exchange = exchangeSupplier.get();
        exchange.getIn().setMessageId(
            Long.toString(event.getMetadata().getKey()) + "-" + event.getMetadata().getPosition());
        exchange.getIn().setHeaders(createHeader.apply(event.getMetadata()));

        JobState state = null;
        if (record instanceof io.zeebe.client.api.events.JobEvent)
        try {
            // state might not be available and throw UnsupportedOperation.
            state = ((io.zeebe.client.api.events.JobEvent)record).getState();

        } catch (RuntimeException e) {
            // ignore
        }
        exchange.getIn().setHeader("state", state.name());
        exchange.getIn().setBody(record);

        return exchange;
    }

}

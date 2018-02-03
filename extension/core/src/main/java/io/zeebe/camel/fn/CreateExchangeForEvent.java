package io.zeebe.camel.fn;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import io.zeebe.client.event.Event;
import io.zeebe.client.event.EventMetadata;
import org.apache.camel.Exchange;

public class CreateExchangeForEvent implements Function<Event, Exchange>
{
    public static final Function<EventMetadata, Map<String, Object>> createHeader = meta -> {
        final Map<String, Object> header = new HashMap<>();

        header.put("topicName", meta.getTopicName());
        header.put("partitionId", meta.getPartitionId());
        header.put("key", meta.getKey());
        header.put("position", meta.getPosition());
        header.put("eventType", meta.getType());

        return header;
    };

    private final Supplier<Exchange> exchangeSupplier;

    public CreateExchangeForEvent(final Supplier<Exchange> exchangeSupplier)
    {
        this.exchangeSupplier = exchangeSupplier;
    }

    @Override
    public Exchange apply(final Event event)
    {
        final Exchange exchange = exchangeSupplier.get();
        exchange.getIn().setHeaders(createHeader.apply(event.getMetadata()));

        String state = null;
        try
        {
            state = event.getState();
        }
        catch (RuntimeException e)
        {
            // ignore
        }
        exchange.getIn().setHeader("state", state);
        exchange.getIn().setMessageId(Long.toString(event.getMetadata().getKey()));

        exchange.getIn().setBody(event);

        return exchange;
    }
}

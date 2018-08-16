package io.zeebe.camel.fn;

import java.util.function.Function;
import org.apache.camel.Exchange;

public class CreateExchangeForEvent implements Function<Object, Exchange> {
// FIXME: implement
//    public static final Function<EventMetadata, Map<String, Object>> createHeader = meta -> {
//        final Map<String, Object> header = new HashMap<>();
//
//        header.put("topicName", meta.getTopicName());
//        header.put("partitionId", meta.getPartitionId());
//        header.put("key", meta.getKey());
//        header.put("position", meta.getPosition());
//        header.put("eventType", meta.getType());
//
//        return header;
//    };
//
//    private final Supplier<Exchange> exchangeSupplier;
//
//    public CreateExchangeForEvent(final Supplier<Exchange> exchangeSupplier) {
//        this.exchangeSupplier = exchangeSupplier;
//    }
//
//    @Override
//    public Exchange apply(final Event event) {
//        final Exchange exchange = exchangeSupplier.get();
//        exchange.getIn().setMessageId(
//            Long.toString(event.getMetadata().getKey()) + "-" + event.getMetadata().getPosition());
//        exchange.getIn().setHeaders(createHeader.apply(event.getMetadata()));
//
//        String state = null;
//        try {
//            // state might not be available and throw UnsupportedOperation.
//            state = event.getState();
//
//        } catch (RuntimeException e) {
//            // ignore
//        }
//        exchange.getIn().setHeader("state", state);
//        exchange.getIn().setBody(event);
//
//        return exchange;
//    }

    @Override
    public Exchange apply(Object o) {
        throw new UnsupportedOperationException();
    }
}

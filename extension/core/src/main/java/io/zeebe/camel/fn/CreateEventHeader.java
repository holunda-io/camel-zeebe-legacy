package io.zeebe.camel.fn;

import io.zeebe.camel.api.event.MessageHeader;
import java.util.Map;
import java.util.function.BiFunction;

public class CreateEventHeader implements BiFunction<Map<String,Object    >, String, MessageHeader> {

    // FIXME: implement
//    @Override
//    public MessageHeader apply(final EventMetadata eventMetadata, final String state) {
//        return MessageHeader.builder()
//            .topicName(eventMetadata.getTopicName())
//            .position(eventMetadata.getPosition())
//            .partitionId(eventMetadata.getPartitionId())
//            .key(eventMetadata.getKey())
//            .type(eventMetadata.getType().name())
//            .state(state)
//            .build();
//    }

    @Override
    public MessageHeader apply(Map<String, Object> stringObjectMap, String s) {
        return null;
    }
}

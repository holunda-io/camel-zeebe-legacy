package io.zeebe.camel.fn;

import io.zeebe.camel.api.event.MessageHeader;
import java.util.Map;
import java.util.function.BiFunction;

public class CreateEventHeader implements BiFunction<Map<String,Object>, String, MessageHeader> {

    // FIXME: implement
    @Override
    public MessageHeader apply(final Map<String,Object> eventMetadata, final String state) {
        return MessageHeader.builder()
            .topicName((String) eventMetadata.get("topic"))
            .position(eventMetadata.getPosition())
            .partitionId(eventMetadata.getPartitionId())
            .key(eventMetadata.getKey())
            .type(eventMetadata.getType().name())
            .state(state)
            .build();
    }

}

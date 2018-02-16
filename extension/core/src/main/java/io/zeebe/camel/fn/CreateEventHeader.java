package io.zeebe.camel.fn;

import java.util.function.BiFunction;

import io.zeebe.camel.api.event.MessageHeader;
import io.zeebe.client.event.EventMetadata;

public class CreateEventHeader implements BiFunction<EventMetadata, String, MessageHeader>
{
    @Override
    public MessageHeader apply(final EventMetadata eventMetadata, final String state)
    {
        return MessageHeader.builder()
                            .topicName(eventMetadata.getTopicName())
                            .position(eventMetadata.getPosition())
                            .partitionId(eventMetadata.getPartitionId())
                            .key(eventMetadata.getKey())
                            .type(eventMetadata.getType().name())
                            .state(state)
                            .build();
    }
}

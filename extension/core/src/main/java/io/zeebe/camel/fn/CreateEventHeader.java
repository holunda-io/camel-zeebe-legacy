package io.zeebe.camel.fn;

import java.util.function.BiFunction;

import io.zeebe.camel.api.event.EventHeader;
import io.zeebe.client.event.EventMetadata;

public class CreateEventHeader implements BiFunction<EventMetadata, String, EventHeader>
{
    @Override
    public EventHeader apply(final EventMetadata eventMetadata, final String state)
    {
        return EventHeader.builder()
                          .topicName(eventMetadata.getTopicName())
                          .position(eventMetadata.getPosition())
                          .partitionId(eventMetadata.getPartitionId())
                          .key(eventMetadata.getKey())
                          .type(eventMetadata.getType().name())
                          .state(state)
                          .build();
    }
}

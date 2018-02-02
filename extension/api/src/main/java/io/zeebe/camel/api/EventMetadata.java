package io.zeebe.camel.api;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EventMetadata
{
    /**
     * @return the name of the topic this event as published on
     */
    private final String topicName;

    /**
     * @return the id of the topic partition this event was published on
     */
    private final int partitionId;

    /**
     * @return the unique position the event has in the topic. Events are ordered by position.
     */
    private final long position;

    /**
     * @return the key of the event on this topic. Multiple events can have the same key if they
     *   reflect state of the same logical entity. Keys are unique for the combination of topic, partition and entity type.
     */
    private final long key;

}

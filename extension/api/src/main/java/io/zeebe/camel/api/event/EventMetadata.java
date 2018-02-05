package io.zeebe.camel.api.event;

import lombok.Builder;
import lombok.Value;

/**
 * Event meta data.
 */
@Value
@Builder
public class EventMetadata
{
    /**
     * The name of the topic this event as published on
     */
    private final String topicName;

    /**
     * The id of the topic partition this event was published on
     */
    private final int partitionId;

    /**
     * The unique position the event has in the topic. Events are ordered by position.
     */
    private final long position;

    /**
     * The key of the event on this topic. Multiple events can have the same key if they
     *   reflect state of the same logical entity. Keys are unique for the combination of topic, partition and entity type.
     */
    private final long key;

}

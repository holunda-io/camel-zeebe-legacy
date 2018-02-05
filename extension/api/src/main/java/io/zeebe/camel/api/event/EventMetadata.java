package io.zeebe.camel.api.event;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class EventMetadata
{
    /**
     * @return the name of the topic this event as published on
     */
    @NonNull
    private final String topicName;

    /**
     * @return the id of the topic partition this event was published on
     */
    @NonNull
    private final Integer partitionId;

    /**
     * @return the unique position the event has in the topic. Events are ordered by position.
     */
    @NonNull
    private final Long position;

    /**
     * @return the key of the event on this topic. Multiple events can have the same key if they
     *   reflect state of the same logical entity. Keys are unique for the combination of topic, partition and entity type.
     */
    @NonNull
    private final Long key;

}

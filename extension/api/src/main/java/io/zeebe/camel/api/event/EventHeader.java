package io.zeebe.camel.api.event;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Value;

/**
 * Event meta data.
 */
@Value
@Builder
public class EventHeader
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

    private final String type;

    private final String state;

    public Map<String,Object> toMap() {
        final Map<String, Object> header = new HashMap<>();

        header.put("topicName", topicName);
        header.put("partitionId", partitionId);
        header.put("key", key);
        header.put("position", position);
        header.put("type", type);
        header.put("state", state);

        return header;
    }
}

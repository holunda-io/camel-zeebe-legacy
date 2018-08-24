package io.zeebe.camel.api.event;

import io.zeebe.protocol.impl.RecordMetadata;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

/**
 * Event meta data.
 */
@Value
@Builder
public class MessageHeader {

    public static final String TOPIC_NAME = "topicName";
    public static final String PARTITION_ID = "partitionId";
    public static final String KEY = "key";
    public static final String POSITION = "position";
    public static final String TYPE = "type";
    public static final String STATE = "state";


    public static MessageHeader from(RecordMetadata metadata) {
        return MessageHeader.builder()
            .topicName(metadata.get)
            .partitionId((Integer) map.get(PARTITION_ID))
            .key((Long) map.get(KEY))
            .position((Long) map.get(POSITION))
            .type((String) map.get(TYPE))
            .state((String) map.get(STATE))
            .build();
    }

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
     * The key of the event on this topic. Multiple events can have the same key if they reflect state
     * of the same logical entity. Keys are unique for the combination of topic, partition and entity
     * type.
     */
    private final long key;

    private final String type;

    private final String state;

    public Map<String, Object> toMap() {
        final Map<String, Object> header = new HashMap<>();

        header.put(TOPIC_NAME, topicName);
        header.put(PARTITION_ID, partitionId);
        header.put(KEY, key);
        header.put(POSITION, position);
        header.put(TYPE, type);
        header.put(STATE, state);

        return header;
    }
}

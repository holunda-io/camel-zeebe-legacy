package io.zeebe.camel.api.event;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TaskEvent implements ZeebeEvent
{
    public static final String PAYLOAD = "payloadSource";

    @JsonIgnore
    private final MessageHeader metadata;

    /**
     * The name of the state in the event's lifecycle. The lifecycle is different for each type of event.
     */
    private final String state;

    /**
     * The task's type.
     */
    private final String type;

    /**
     * Broker-defined headers associated with this task. For example, if this task is
     *   created in the context of workflow instance, the header provide context information
     *   on which activity is executed, etc.
     */
    private final  Map<String, Object> headers;

    /**
     * User-defined headers associated with this task.
     */
    private final Map<String, Object> customHeaders;

    /**
     * The lock owner.
     */
    private final String lockOwner;

    /**
     * Remaining retries.
     */
    private final Integer retries;

    /**
     * The time until when the task is locked
     *   and can be exclusively processed by this client.
     */
    private final long lockTime;

    /**
     * JSON-formatted payload.
     */
    @JsonProperty(PAYLOAD)
    private final String payload;

    @JsonProperty("payload")
    private final String payloadPck;
}

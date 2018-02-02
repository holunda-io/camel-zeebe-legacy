package io.zeebe.camel.api;

import java.time.Instant;
import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TaskEvent
{

    /**
     * @return the events metadata, such as its key or the topic and partition it belongs to
     */
    private final EventMetadata metadata;

    /**
     * @return the name of the state in the event's lifecycle. The lifecycle is different for each type
     *   of event.
     */
    private final String state;
    /**
     * @return the task's type
     */
    private final String type;

    /**
     * @return broker-defined headers associated with this task. For example, if this task is
     *   created in the context of workflow instance, the header provide context information
     *   on which activity is executed, etc.
     */
    private final  Map<String, Object> headers;

    /**
     * @return user-defined headers associated with this task
     */
    private final Map<String, Object> customHeaders;

    /**
     * @return the lock owner
     */
    private final String lockOwner;

    /**
     * @return remaining retries
     */
    private final Integer retries;

    /**
     * @return the time until when the task is locked
     *   and can be exclusively processed by this client.
     */
    private final long lockTime;

    /**
     * @return JSON-formatted payload
     */
    private final String getPayload;
}

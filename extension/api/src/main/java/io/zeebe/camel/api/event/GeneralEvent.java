package io.zeebe.camel.api.event;

import lombok.Builder;
import lombok.Value;

/**
 * Represents a general Zeebe event.
 */
@Value
@Builder
public class GeneralEvent implements ZeebeEvent
{
    /**
     * The events metadata, such as its key or the topic and partition it belongs to
     */
    private final MessageHeader metadata;

    /**
     * The name of the state in the event's lifecycle. The lifecycle is different for each type of event.
     */
    private final String state;

    /**
     * Event encoded as JSON.
     */
    private final String json;
}

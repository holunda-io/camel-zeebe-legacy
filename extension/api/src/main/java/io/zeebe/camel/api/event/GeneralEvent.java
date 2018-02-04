package io.zeebe.camel.api.event;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GeneralEvent implements ZeebeEvent
{
    /**
     * @return the events metadata, such as its key or the topic and partition it belongs to
     */
    private EventMetadata metadata;

    /**
     * @return the name of the state in the event's lifecycle. The lifecycle is different for each type
     *   of event.
     */
    String state;

    /**
     * @return event encoded as JSON
     */
    String json;
}

package io.zeebe.camel.consumer;

import io.zeebe.client.event.Event;
import io.zeebe.client.event.GeneralEvent;
import io.zeebe.client.event.UniversalEventHandler;

public interface EventHandlerAdapter<E extends Event>
{

    static EventHandlerAdapter<GeneralEvent> universalEventHandler (final UniversalEventHandler handler) {
        return event -> handler.handle(event);
    }

    void handle(E event) throws Exception;

}

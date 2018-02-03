package io.zeebe.camel.fn;

import io.zeebe.client.event.Event;
import io.zeebe.client.event.GeneralEvent;
import io.zeebe.client.event.TaskEvent;
import io.zeebe.client.event.UniversalEventHandler;
import io.zeebe.client.task.TaskHandler;

public interface EventHandlerAdapter<E extends Event>
{

    static EventHandlerAdapter<GeneralEvent> universalEventHandler (final UniversalEventHandler handler) {
        return event -> handler.handle(event);
    }

    static EventHandlerAdapter<TaskEvent> taskHandler(final TaskHandler handler) {
        return event -> handler.handle(null, event);
    }

    void handle(E event) throws Exception;

}

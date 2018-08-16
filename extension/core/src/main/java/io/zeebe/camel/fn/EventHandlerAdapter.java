package io.zeebe.camel.fn;

import io.zeebe.client.api.events.JobEvent;
import io.zeebe.client.api.record.Record;
import io.zeebe.client.api.subscription.JobHandler;

public interface EventHandlerAdapter<E extends Record> {

    static EventHandlerAdapter<Record> universalEventHandler() {
        throw new UnsupportedOperationException();
    // FIXME: implement
//        final UniversalEventHandler handler) {
//        return event -> handler.handle(event);
    }

    static EventHandlerAdapter<JobEvent> taskHandler(final JobHandler handler) {
        return event -> handler.handle(null, event);
    }

    void handle(E event) throws Exception;

}

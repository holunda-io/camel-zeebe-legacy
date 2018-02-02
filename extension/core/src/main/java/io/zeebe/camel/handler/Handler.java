package io.zeebe.camel.handler;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.zeebe.camel.handler.task.TaskEndpoint;
import io.zeebe.camel.handler.universal.UniversalEventEndpoint;
import io.zeebe.client.event.UniversalEventHandler;
import io.zeebe.client.task.TaskHandler;
import lombok.Getter;

/**
 * Each supported handler is listed here.
 */
@Getter
public enum Handler
{
    TASK(TaskEndpoint.SUBJECT, TaskHandler.class),
    UNIVERSAL_EVENT(UniversalEventEndpoint.SUBJECT, UniversalEventHandler.class),
    //
    ;

    public static final Map<String, Handler> VALUES = Stream.of(Handler.values()).collect(Collectors.toMap(Handler::getSubject, e -> e));

    private final String subject;
    private final Class<?> zeebeHandler;

    Handler(String subject, Class<?> zeebeHandler)
    {
        this.subject = subject;

        this.zeebeHandler = zeebeHandler;
    }

}

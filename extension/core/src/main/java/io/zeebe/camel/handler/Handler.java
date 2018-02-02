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
    TASK(TaskEndpoint.OPERATION, TaskHandler.class),
    UNIVERSAL_EVENT(UniversalEventEndpoint.OPERATION, UniversalEventHandler.class),
    //
    ;

    public static final Map<String, Handler> VALUES = Stream.of(Handler.values()).collect(Collectors.toMap(Handler::getOperation, e -> e));

    private final String operation;
    private final Class<?> zeebeHandler;

    Handler(String operation, Class<?> zeebeHandler)
    {

        this.operation = operation;
        this.zeebeHandler = zeebeHandler;
    }

}

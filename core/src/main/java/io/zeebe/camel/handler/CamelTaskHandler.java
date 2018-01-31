package io.zeebe.camel.handler;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import io.zeebe.client.TasksClient;
import io.zeebe.client.event.TaskEvent;
import io.zeebe.client.task.TaskHandler;
import io.zeebe.client.task.cmd.CompleteTaskCommand;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CamelTaskHandler implements TaskHandler
{

    public static CamelTaskHandler of(UnaryOperator<String> function) {
        return new CamelTaskHandler(function, null);
    }

    public static CamelTaskHandler of(Consumer<String> consumer) {
        return new CamelTaskHandler(null, consumer);
    }

    private final UnaryOperator<String> function;
    private final Consumer<String> consumer;

    private CamelTaskHandler(UnaryOperator<String> function, Consumer<String> consumer)
    {
        this.function = function;
        this.consumer = consumer;
    }

    @Override
    public void handle(TasksClient client, TaskEvent taskEvent)
    {
        final String in = taskEvent.getPayload();
        log.info("incoming: {}", in);

        CompleteTaskCommand command = client.complete(taskEvent);

        if (function != null) {
            String out = function.apply(in);
            log.info("outgoing: {}", out);
            command.payload(function.apply(in));
        } else {
            command.withoutPayload();
        }

        TaskEvent execute = command.execute();

        log.info("completed: {}", execute);
    }
}

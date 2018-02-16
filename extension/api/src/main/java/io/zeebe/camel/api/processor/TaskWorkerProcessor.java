package io.zeebe.camel.api.processor;

import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zeebe.camel.api.command.TaskCommand;
import io.zeebe.camel.api.event.TaskEvent;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public interface TaskWorkerProcessor extends Processor, Function<TaskEvent, TaskCommand>
{

    static TaskWorkerProcessor processor(final Function<TaskEvent, TaskCommand> worker) {
        return taskEvent -> worker.apply(taskEvent);
    }

    @Override
    default void process(final Exchange exchange) throws Exception
    {
        final Message in = exchange.getIn();

        final ObjectMapper mapper = new ObjectMapper();

        TaskEvent taskEvent = mapper.readValue(in.getBody(String.class), TaskEvent.class);

        final TaskCommand command = apply(taskEvent);

        String json = mapper.writeValueAsString(command);
        in.setBody(json);
    }
}

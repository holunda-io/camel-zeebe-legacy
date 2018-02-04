package io.zeebe.camel.handler.task;

import io.zeebe.camel.ZeebeProducer;
import io.zeebe.camel.api.command.CompleteTaskCommand;
import io.zeebe.client.event.TaskEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;

@Slf4j
public class TaskProducer extends ZeebeProducer<TaskEndpoint>
{

    public TaskProducer(final TaskEndpoint endpoint)
    {
        super(endpoint);
    }

    @Override
    public void process(final Exchange exchange) throws Exception
    {
        // TODO: switch operation based on command (api)

        final CompleteTaskCommand command = exchange.getIn().getBody(CompleteTaskCommand.class);
        final TaskEvent taskEvent = TaskConverter.convert(command.getTask());

        io.zeebe.client.task.cmd.CompleteTaskCommand complete = endpoint.getClient().tasks().complete(taskEvent);

        if (command.hasPayload()) {
            complete.payload(command.getPayload());
        } else {
            complete.withoutPayload();
        }

        complete.execute();
    }
}

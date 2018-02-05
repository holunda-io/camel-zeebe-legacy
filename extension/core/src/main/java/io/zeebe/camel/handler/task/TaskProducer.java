package io.zeebe.camel.handler.task;

import io.zeebe.camel.ZeebeProducer;
import io.zeebe.camel.api.command.CompleteTaskCommand;
import io.zeebe.camel.api.command.FailureTaskCommand;
import io.zeebe.camel.api.command.TaskCommand;
import io.zeebe.client.event.TaskEvent;
import io.zeebe.client.task.cmd.FailTaskCommand;
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
        final TaskCommand command = exchange.getIn().getBody(TaskCommand.class);
        final TaskEvent taskEvent = TaskConverter.convert(command.getTask());

        // type-based switch
        if (command instanceof CompleteTaskCommand) {
            process(taskEvent, (CompleteTaskCommand) command);
        } else if (command instanceof FailureTaskCommand) {
            process(taskEvent, (FailureTaskCommand)command);
        } else {
            process(taskEvent, command);
        }
    }



    void process(final TaskEvent taskEvent, final CompleteTaskCommand command) {
        log.trace("Processing complete command");

        io.zeebe.client.task.cmd.CompleteTaskCommand complete = endpoint.getClient().tasks().complete(taskEvent);

        if (command.hasPayload()) {
            complete.payload(command.getPayload());
        } else {
            complete.withoutPayload();
        }
        complete.execute();
    }

    void process(final TaskEvent taskEvent, final FailureTaskCommand command) {
        log.trace("Processing failure command");
        io.zeebe.client.task.cmd.FailTaskCommand fail = endpoint.getClient().tasks().fail(taskEvent);
        fail.execute();
    }


    public void process(final TaskEvent taskEvent, final TaskCommand command) {
        log.info("Dispatching unknown command type {} for task {}", command.getClass().getSimpleName(), taskEvent);
    }

}

package io.zeebe.camel.handler.task;

import java.util.Optional;

import io.zeebe.camel.ZeebeProducer;
import io.zeebe.camel.api.command.TaskCommand;
import io.zeebe.camel.processor.JsonToPrepareCommandProcessor;
import io.zeebe.camel.processor.JsonToPrepareCommandProcessor.PrepareCommand;
import io.zeebe.client.event.TaskEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;

@Slf4j
public class TaskProducer extends ZeebeProducer<TaskEndpoint>
{
    private final JsonToPrepareCommandProcessor prepareCommandProcessor;

    public TaskProducer(final TaskEndpoint endpoint)
    {
        super(endpoint);
        this.prepareCommandProcessor = new JsonToPrepareCommandProcessor(endpoint.getClient().getObjectMapper());
    }

    @Override
    public void process(final Exchange exchange) throws Exception
    {
        prepareCommandProcessor.process(exchange);
        final PrepareCommand command = exchange.getIn().getBody(PrepareCommand.class);

        switch (command.getCommandType()) {
        case COMPLETE:
            process(command.getTask(), Optional.ofNullable(command.getPayload()));
            break;
        case FAIL:
            process(command.getTask());
            break;
            default:
                log.error("Dispatching unknown command {}", command);
        }

    }

    void process(final TaskEvent taskEvent, Optional<String> payload) {
        log.trace("Processing complete command");

        io.zeebe.client.task.cmd.CompleteTaskCommand complete = endpoint.getClient().tasks().complete(taskEvent);

        if (payload.isPresent()) {
            // FIXME: for zeebe, the payload must be enclosed with {}, but its not. Here we just fake it!
            complete.payload("{" + payload.get() +"}");
        } else {
            complete.withoutPayload();
        }
        complete.execute();
    }

    void process(final TaskEvent taskEvent) {
        log.trace("Processing failure command");
        io.zeebe.client.task.cmd.FailTaskCommand fail = endpoint.getClient().tasks().fail(taskEvent);
        fail.execute();
    }

}

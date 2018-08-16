package io.zeebe.camel.handler.task;

import io.zeebe.camel.ZeebeProducer;
import io.zeebe.camel.api.command.CompleteJobCommand;
import io.zeebe.camel.api.event.JobEvent;
import io.zeebe.camel.processor.JsonToPrepareCommandProcessor;
import io.zeebe.camel.processor.JsonToPrepareCommandProcessor.PrepareCommand;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;

@Slf4j
public class TaskProducer extends ZeebeProducer<TaskEndpoint> {

    private final JsonToPrepareCommandProcessor prepareCommandProcessor;

    public TaskProducer(final TaskEndpoint endpoint) {
        super(endpoint);
        this.prepareCommandProcessor = null;

        // FIXME: implement
//        new JsonToPrepareCommandProcessor(
//            endpoint.getClient().getObjectMapper());
    }

    @Override
    public void process(final Exchange exchange) throws Exception {
        prepareCommandProcessor.process(exchange);
        final PrepareCommand command = exchange.getIn().getBody(PrepareCommand.class);

        // FIXME: implement
//        switch (command.getCommandType()) {
//            case COMPLETE:
//                process(command.getJob(), Optional.ofNullable(command.getPayload()));
//                break;
//            case FAIL:
//                process(command.getJob());
//                break;
//            default:
//                log.error("Dispatching unknown command {}", command);
//        }

    }

    void process(final JobEvent taskEvent, Optional<String> payload) {
        log.trace("Processing complete command");

        CompleteJobCommand complete = null; // FIXME: implement endpoint.getClient().tasks().complete(taskEvent);
// FIXME: implement
//        if (payload.isPresent()) {
//            // FIXME: for zeebe, the payload must be enclosed with {}, but its not. Here we just fake it!
//            complete.payload("{" + payload.get() + "}");
//        } else {
//            complete.withoutPayload();
//        }
//        complete.execute();
    }
//
//    void process(final JobEvent taskEvent) {
//        log.trace("Processing failure command");
//        FaiJ fail = endpoint.getClient().tasks()
//            .fail(taskEvent);
//        fail.execute();
//    }

}

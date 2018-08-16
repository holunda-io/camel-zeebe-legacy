package io.zeebe.camel.api.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zeebe.camel.api.command.JobCommand;
import io.zeebe.camel.api.event.JobEvent;
import java.util.function.Function;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public interface TaskWorkerProcessor extends Processor, Function<JobEvent, JobCommand> {

    static TaskWorkerProcessor processor(final Function<JobEvent, JobCommand> worker) {
        return taskEvent -> worker.apply(taskEvent);
    }

    @Override
    default void process(final Exchange exchange) throws Exception {
        final Message in = exchange.getIn();

        final ObjectMapper mapper = new ObjectMapper();

        JobEvent jobEvent = mapper.readValue(in.getBody(String.class), JobEvent.class);

        final JobCommand command = apply(jobEvent);

        String json = mapper.writeValueAsString(command);
        in.setBody(json);
    }
}

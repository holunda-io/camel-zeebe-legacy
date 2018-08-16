package io.zeebe.camel.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zeebe.camel.api.command.CommandType;
import io.zeebe.camel.api.event.MessageHeader;
import io.zeebe.client.api.record.ZeebeObjectMapper;
import io.zeebe.client.impl.event.JobEventImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

@RequiredArgsConstructor
public class JsonToPrepareCommandProcessor implements Processor {

    private final ZeebeObjectMapper mapper;

    @Data
    public static class PrepareCommand {

        private JobEventImpl job;
        private String payload;
        private CommandType commandType;
    }

    @Override
    public void process(final Exchange exchange) throws Exception {
        final Message in = exchange.getIn();

        final String body = in.getBody(String.class);

        final PrepareCommand command =     null; // FIXME mapper.readValue(body, PrepareCommand.class);
        command.getJob().setKey(in.getHeader(MessageHeader.KEY, Long.class));
        command.getJob().setSourceRecordPosition(in.getHeader(MessageHeader.POSITION, Long.class));
        command.getJob().setPartitionId(in.getHeader(MessageHeader.PARTITION_ID, Integer.class));
        command.getJob().setTopicName(in.getHeader(MessageHeader.TOPIC_NAME, String.class));

        in.setBody(command);
    }
}


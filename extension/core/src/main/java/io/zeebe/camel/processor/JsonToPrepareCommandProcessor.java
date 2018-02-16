package io.zeebe.camel.processor;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zeebe.camel.api.command.CommandType;
import io.zeebe.camel.api.event.MessageHeader;
import io.zeebe.client.event.impl.TaskEventImpl;
import lombok.Data;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class JsonToPrepareCommandProcessor implements Processor
{
    public JsonToPrepareCommandProcessor(ObjectMapper mapper)
    {
        this.mapper = mapper;
    }

    @Data
    public static class PrepareCommand
    {
        private TaskEventImpl task;
        private String payload;
        private CommandType commandType;

    }

    private final ObjectMapper mapper;

    @Override
    public void process(final Exchange exchange) throws Exception
    {
        final Message in = exchange.getIn();

        final String body = in.getBody(String.class);

        final PrepareCommand command = mapper.readValue(body, PrepareCommand.class);
        command.getTask().setKey(in.getHeader(MessageHeader.KEY, Long.class));
        command.getTask().setEventPosition(in.getHeader(MessageHeader.POSITION, Long.class));
        command.getTask().setPartitionId(in.getHeader(MessageHeader.PARTITION_ID, Integer.class));
        command.getTask().setTopicName(in.getHeader(MessageHeader.TOPIC_NAME, String.class));

        in.setBody(command);
    }
}


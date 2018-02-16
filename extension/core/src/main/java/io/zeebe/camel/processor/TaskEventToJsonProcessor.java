package io.zeebe.camel.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.zeebe.camel.api.event.MessageHeader;
import io.zeebe.camel.fn.CreateEventHeader;
import io.zeebe.client.event.impl.TaskEventImpl;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 * Reads a {@link TaskEventImpl} from the {@link Exchange} and puts back a converted json representation.
 * Also the message header is filled with the {@link io.zeebe.client.event.EventMetadata} information.
 */
public class TaskEventToJsonProcessor implements Processor
{
    private final ObjectMapper mapper;
    private final CreateEventHeader createEventHeader = new CreateEventHeader();

    public TaskEventToJsonProcessor()
    {
        // TODO: strange, when we use the zeebe mapper setup here, we get an UnsupportedOperationException
        // org.msgpack.jackson.dataformat.MessagePackFactory.createGenerator(MessagePackFactory.java:90)
        this.mapper = new ObjectMapper();
    }

    @Override
    public void process(final Exchange exchange) throws Exception
    {
        final Message in = exchange.getIn();

        final TaskEventImpl task = in.getBody(TaskEventImpl.class);

        final MessageHeader header = createEventHeader.apply(task.getMetadata(), task.getState());

        final String body = mapper.writeValueAsString(task);
        final String payload = task.getPayload();

        final ObjectNode node = (ObjectNode) mapper.readTree(body);
        node.put(io.zeebe.camel.api.event.TaskEvent.PAYLOAD, payload);

        in.setHeaders(header.toMap());
        in.setBody(mapper.writeValueAsString(node));
    }
}

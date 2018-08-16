package io.zeebe.camel.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.zeebe.camel.api.event.JobEvent;
import io.zeebe.camel.api.event.MessageHeader;
import io.zeebe.camel.fn.CreateEventHeader;
import io.zeebe.client.impl.event.JobEventImpl;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 * Reads a {@link JobEventImpl} from the {@link Exchange} and puts back a converted json
 * representation. Also the message header is filled with the MetaD information.
 */
public class TaskEventToJsonProcessor implements Processor {

    private final ObjectMapper mapper;
    private final CreateEventHeader createEventHeader = new CreateEventHeader();

    public TaskEventToJsonProcessor() {
        // TODO: strange, when we use the zeebe mapper setup here, we get an UnsupportedOperationException
        // org.msgpack.jackson.dataformat.MessagePackFactory.createGenerator(MessagePackFactory.java:90)
        this.mapper = new ObjectMapper();
    }

    @Override
    public void process(final Exchange exchange) throws Exception {
        // FIXME: implement
//        final Message in = exchange.getIn();
//
//        final JobEventImpl jobEvent = in.getBody(JobEventImpl.class);
//
//        final MessageHeader header = createEventHeader.apply(jobEvent.getHeaders(), jobEvent.getState());
//
//        final String body = mapper.writeValueAsString(jobEvent);
//        final String payload = jobEvent.getPayload();
//
//        final ObjectNode node = (ObjectNode) mapper.readTree(body);
//        node.put(JobEvent.PAYLOAD, payload);
//
//        in.setHeaders(header.toMap());
//        in.setBody(mapper.writeValueAsString(node));
    }
}

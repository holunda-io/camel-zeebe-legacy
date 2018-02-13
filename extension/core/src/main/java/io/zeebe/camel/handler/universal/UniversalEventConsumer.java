package io.zeebe.camel.handler.universal;

import io.zeebe.camel.fn.CreateEventHeader;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.zeebe.camel.ZeebeConsumer;
import io.zeebe.camel.api.event.EventHeader;
import io.zeebe.client.TopicsClient;
import io.zeebe.client.event.TopicSubscription;
import io.zeebe.client.event.UniversalEventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Subscribes as a {@link UniversalEventHandler} and publishes all events to the route.
 */
@Slf4j
public class UniversalEventConsumer extends ZeebeConsumer<UniversalEventEndpoint, UniversalEventHandler, TopicSubscription>
{
    private final TopicsClient client;
    private final String topic;
    private final String name;

    private final CreateEventHeader createEventHeader = new CreateEventHeader();

    public UniversalEventConsumer(final UniversalEventEndpoint endpoint, final Processor processor)
    {
        super(endpoint, processor);
        this.client = endpoint.getClient().topics();
        this.topic = endpoint.getTopic();
        this.name = endpoint.getName();
    }

    @Override
    protected UniversalEventHandler createHandler()
    {
        return e -> {
            final Exchange exchange = endpoint.createExchange();

            String json = e.getJson();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);
            String state = node.at("/state").asText();

            exchange.getIn().setHeaders(createEventHeader.apply(e.getMetadata(), state).toMap());
            exchange.getIn().setBody(json);

            getProcessor().process(exchange);
        };
    }

    @Override
    protected TopicSubscription createSubscription(UniversalEventHandler handler)
    {
        return client.newSubscription(topic).name(name).startAtHeadOfTopic().handler(handler).open();
    }

}

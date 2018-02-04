package io.zeebe.camel.handler.universal;

import io.zeebe.camel.ZeebeConsumer;
import io.zeebe.client.TopicsClient;
import io.zeebe.client.event.TopicSubscription;
import io.zeebe.client.event.UniversalEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Processor;

/**
 * Subscribes as a {@link UniversalEventHandler} and publishes all events to the route.
 */
@Slf4j
public class UniversalEventConsumer extends ZeebeConsumer<UniversalEventEndpoint, UniversalEventHandler, TopicSubscription>
{
    private final TopicsClient client;
    private final String topic;
    private final String name;

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
        return e -> getProcessor().process(createExchangeForEvent.apply(e));
    }

    @Override
    protected TopicSubscription createSubscription(UniversalEventHandler handler)
    {
        return client.newSubscription(topic).name(name).startAtHeadOfTopic().handler(handler).open();
    }

}

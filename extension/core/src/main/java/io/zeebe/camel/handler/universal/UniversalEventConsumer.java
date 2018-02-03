package io.zeebe.camel.handler.universal;

import io.zeebe.camel.ZeebeConsumer;
import io.zeebe.camel.fn.SubscriptionAdapter;
import io.zeebe.client.TopicsClient;
import io.zeebe.client.event.TopicSubscription;
import io.zeebe.client.event.UniversalEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Processor;

/**
 * The Zeebe consumer.
 */
@Slf4j
public class UniversalEventConsumer extends ZeebeConsumer<UniversalEventEndpoint, UniversalEventHandler, TopicSubscription>
{
    private final TopicsClient client;
    public UniversalEventConsumer(final UniversalEventEndpoint endpoint, final Processor processor)
    {
        super(endpoint, processor);
        log.info("consumer: {}", getClass().getSimpleName());

        this.client = endpoint.getClient().topics();
    }

    @Override
    protected UniversalEventHandler createHandler()
    {
        return e -> getProcessor().process(createExchangeForEvent.apply(e));
    }

    @Override
    protected TopicSubscription createSubscription(UniversalEventHandler handler)
    {
        return client.newSubscription("default-topic").name("dummy").startAtHeadOfTopic().handler(handler).open();
    }

}

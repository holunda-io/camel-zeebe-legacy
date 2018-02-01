package io.zeebe.camel.consumer;

import io.zeebe.camel.consumer.AbstractZeebeConsumer;
import io.zeebe.camel.consumer.SubscriptionAdapter;
import io.zeebe.camel.endpoint.UniversalEventHandlerEndpoint;
import io.zeebe.client.event.UniversalEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Processor;

/**
 * The Zeebe consumer.
 */
@Slf4j
public class UniversalEventHandlerConsumer extends AbstractZeebeConsumer<UniversalEventHandlerEndpoint, UniversalEventHandler>
{
    public UniversalEventHandlerConsumer(final UniversalEventHandlerEndpoint endpoint, final Processor processor)
    {
        super(endpoint, processor);
        log.info("consumer: {}", getClass().getSimpleName());
    }

    @Override
    protected UniversalEventHandler createHandler(final Processor processor)
    {
        return e -> processor.process(createExchangeForEvent.apply(e));
    }

    @Override
    protected SubscriptionAdapter createSubscription(UniversalEventHandler handler)
    {
        return SubscriptionAdapter.of(this.endpoint.getClient().topics().newSubscription("default-topic").name("dummy").startAtHeadOfTopic().handler(handler).open());
    }

}

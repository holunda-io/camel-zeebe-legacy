package io.zeebe.camel.handler.universal;

import io.zeebe.camel.AbstractZeebeConsumer;
import io.zeebe.camel.fn.SubscriptionAdapter;
import io.zeebe.client.event.UniversalEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Processor;

/**
 * The Zeebe consumer.
 */
@Slf4j
public class UniversalEventConsumer extends AbstractZeebeConsumer<UniversalEventEndpoint, UniversalEventHandler>
{
    public UniversalEventConsumer(final UniversalEventEndpoint endpoint, final Processor processor)
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

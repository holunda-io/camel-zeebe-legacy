package io.zeebe.camel;

import java.util.Optional;

import io.zeebe.camel.fn.ClientSupplier;
import io.zeebe.camel.fn.SubscriptionAdapter;
import io.zeebe.camel.fn.CreateExchangeForEvent;
import io.zeebe.client.ZeebeClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

/**
 * An event driven {@link org.apache.camel.Consumer} that creates a subscription via {@link io.zeebe.client.ZeebeClient}
 * and forwards events via {@link org.apache.camel.Exchange}.
 *
 * @param <ZE> type of zeebe endpoint
 * @param <EH> type of eventHandler
 */
@Slf4j
public abstract class AbstractZeebeConsumer<ZE extends AbstractZeebeEndpoint, EH> extends DefaultConsumer implements ClientSupplier
{
    protected final ZE endpoint;
    protected final CreateExchangeForEvent createExchangeForEvent;

    protected SubscriptionAdapter subscriptionAdapter;

    public AbstractZeebeConsumer(final ZE endpoint, final Processor processor)
    {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.createExchangeForEvent = new CreateExchangeForEvent(() -> endpoint.createExchange());
    }

    protected abstract EH createHandler(Processor processor);

    protected abstract SubscriptionAdapter createSubscription(EH handler);

    @Override
    protected void doStart() throws Exception
    {
        EH handler = createHandler(getProcessor());

        subscriptionAdapter = createSubscription(handler);

    }

    @Override
    protected void doStop() throws Exception
    {
        log.info("stopping consumer ....");
        Optional.ofNullable(subscriptionAdapter)
                .filter(SubscriptionAdapter::isNotClosed)
                .ifPresent(SubscriptionAdapter::close);
    }

    @Override
    public ZeebeClient getClient()
    {
        return endpoint.getClient();
    }
}

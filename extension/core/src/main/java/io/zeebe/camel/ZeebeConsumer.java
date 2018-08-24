package io.zeebe.camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zeebe.camel.fn.ClientSupplier;
import io.zeebe.camel.fn.CreateExchangeForEvent;
import io.zeebe.camel.fn.SubscriptionAdapter;
import io.zeebe.client.api.record.ZeebeObjectMapper;
import io.zeebe.client.impl.ZeebeClientImpl;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

/**
 * An event driven {@link org.apache.camel.Consumer} that creates a subscription via {@link
 * io.zeebe.client.ZeebeClient} and forwards events via {@link org.apache.camel.Exchange}.
 *
 * @param <ZE> type of zeebe endpoint
 * @param <EH> type of eventHandler
 * @param <ES> type of subscription
 */
@Slf4j
public abstract class ZeebeConsumer<ZE extends ZeebeEndpoint, EH, ES> extends
    DefaultConsumer implements ClientSupplier {

    protected final ZE endpoint;
    protected final CreateExchangeForEvent createExchangeForEvent;

    protected SubscriptionAdapter subscriptionAdapter;
    protected final ZeebeObjectMapper mapper;

    public ZeebeConsumer(final ZE endpoint, final Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.createExchangeForEvent = new CreateExchangeForEvent(() -> endpoint.createExchange());
        this.mapper = endpoint.getClient().getObjectMapper();
    }

    protected abstract EH createHandler();

    protected abstract ES createSubscription(EH handler);

    @Override
    protected void doStart() {
        EH handler = createHandler();

        subscriptionAdapter = SubscriptionAdapter.of(createSubscription(handler));
    }

    @Override
    protected void doStop() {
        log.info("Stopping consumer {}.", this.getClass());
        Optional.ofNullable(subscriptionAdapter).filter(SubscriptionAdapter::isNotClosed)
            .ifPresent(SubscriptionAdapter::close);
    }

    @Override
    public ZeebeClientImpl getClient() {
        return endpoint.getClient();
    }
}

package io.zeebe.camel;

import io.zeebe.camel.fn.ClientSupplier;
import io.zeebe.client.ZeebeClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

@Slf4j
public abstract class AbstractZeebeEndpoint extends DefaultEndpoint implements ClientSupplier
{
    protected final EndpointConfiguration configuration;

    // TODO: set in constructor?
    protected final boolean isSingleton = true;

    public AbstractZeebeEndpoint(final EndpointConfiguration configuration)
    {
        super(configuration.getUri(), configuration.getComponent());
        this.configuration = configuration;
    }

    @Override
    public Producer createProducer() throws Exception
    {
        log.debug("createProducer: consumerOnly -> return null");
        return null;
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception
    {
        log.debug("createConsumer: producerOnly -> return null");
        return null;
    }

    @Override
    public ZeebeClient getClient()
    {
        return configuration.getComponent().getClient();
    }

    @Override
    public boolean isSingleton()
    {
        return isSingleton;
    }
}

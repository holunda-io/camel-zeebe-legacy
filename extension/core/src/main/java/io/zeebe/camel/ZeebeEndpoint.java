package io.zeebe.camel;

import io.zeebe.camel.fn.ClientSupplier;
import io.zeebe.client.impl.ZeebeClientImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriPath;

@Slf4j
public abstract class ZeebeEndpoint extends DefaultEndpoint implements ClientSupplier
{
    protected final EndpointConfiguration configuration;

    // TODO: set in constructor?
    protected final boolean isSingleton = true;

    /**
     * The topic
     */
    @UriPath(label = "topic", name = "topic", description = "the topic to connect to.")
    @Metadata(required = "true")
    @Getter
    @Setter
    protected String topic;

    public ZeebeEndpoint(final EndpointConfiguration configuration)
    {
        super(configuration.getUri(), configuration.getComponent());
        this.configuration = configuration;
    }

    @Override
    public Producer createProducer() throws Exception
    {
        log.debug("CreateProducer from abstract implementation: consumerOnly -> return null");
        return null;
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception
    {
        log.debug("CreateConsumer from abstract implementation: producerOnly -> return null");
        return null;
    }

    @Override
    public ZeebeClientImpl getClient()
    {
        return configuration.getComponent().getClient();
    }

    @Override
    public boolean isSingleton()
    {
        return isSingleton;
    }
}

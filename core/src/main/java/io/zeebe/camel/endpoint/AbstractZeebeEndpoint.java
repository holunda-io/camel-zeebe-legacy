package io.zeebe.camel.endpoint;

import io.zeebe.camel.fn.ClientSupplier;
import io.zeebe.camel.ZeebeComponent;
import io.zeebe.client.ZeebeClient;
import org.apache.camel.impl.DefaultEndpoint;

public abstract class AbstractZeebeEndpoint extends DefaultEndpoint implements ClientSupplier
{
    protected final ZeebeComponent zeebeComponent;
    protected final EndpointConfiguration configuration;

    // TODO: set in constructor?
    protected final boolean isSingleton = true;

    public AbstractZeebeEndpoint(final EndpointConfiguration configuration, final ZeebeComponent zeebeComponent)
    {
        super(configuration.getUri(), zeebeComponent);
        this.zeebeComponent = zeebeComponent;
        this.configuration = configuration;
    }

    @Override
    public ZeebeClient getClient()
    {
        return zeebeComponent.getClient();
    }

    @Override
    public boolean isSingleton()
    {
        return isSingleton;
    }
}

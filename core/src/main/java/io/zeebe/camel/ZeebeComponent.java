package io.zeebe.camel;

import java.util.Map;

import io.zeebe.client.ZeebeClient;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link ZeebeEndpoint}.
 */
public class ZeebeComponent extends DefaultComponent implements ClientSupplier
{
    private final ZeebeClient client;

    public ZeebeComponent(ZeebeClient client)
    {
        this.client = client;
    }

    @Override
    protected Endpoint createEndpoint(final String uri, final String remaining, final Map<String, Object> parameters) throws Exception
    {
        Endpoint endpoint = new ZeebeEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }

    @Override
    public ZeebeClient getClient()
    {
        return client;
    }
}

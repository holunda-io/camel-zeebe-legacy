package io.zeebe.camel;

import io.zeebe.camel.AbstractZeebeEndpoint;
import io.zeebe.camel.fn.ClientSupplier;
import io.zeebe.client.ZeebeClient;
import org.apache.camel.impl.DefaultProducer;

public abstract class AbstractZeebeProducer<ZE extends AbstractZeebeEndpoint> extends DefaultProducer
{
    protected final ZE endpoint;

    public AbstractZeebeProducer(final ZE endpoint)
    {
        super(endpoint);
        this.endpoint = endpoint;
    }


}

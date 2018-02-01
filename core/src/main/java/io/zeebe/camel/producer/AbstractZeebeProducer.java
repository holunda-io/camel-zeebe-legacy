package io.zeebe.camel.producer;

import io.zeebe.camel.endpoint.AbstractZeebeEndpoint;
import org.apache.camel.Endpoint;
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

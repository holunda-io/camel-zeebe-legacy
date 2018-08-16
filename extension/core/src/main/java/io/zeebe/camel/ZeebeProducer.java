package io.zeebe.camel;

import org.apache.camel.impl.DefaultProducer;

public abstract class ZeebeProducer<ZE extends ZeebeEndpoint> extends DefaultProducer {

    protected final ZE endpoint;

    public ZeebeProducer(final ZE endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

}

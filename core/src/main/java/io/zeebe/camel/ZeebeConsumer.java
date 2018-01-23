package io.zeebe.camel;

import org.apache.camel.impl.EventDrivenPollingConsumer;

/**
 * The Zeebe consumer.
 */
public class ZeebeConsumer extends EventDrivenPollingConsumer {
    private final ZeebeEndpoint endpoint;

    public ZeebeConsumer(ZeebeEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

}

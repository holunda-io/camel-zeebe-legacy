package io.zeebe.camel;

import org.apache.camel.Processor;
import org.apache.camel.impl.EventDrivenPollingConsumer;

/**
 * The Zeebe consumer.
 */
public class ZeebeConsumer extends EventDrivenPollingConsumer {
    private final ZeebeEndpoint endpoint;
    private final Processor processor;

    public ZeebeConsumer(ZeebeEndpoint endpoint, Processor processor) {
        super(endpoint);

        this.endpoint = endpoint;
        this.processor = processor;
    }

}

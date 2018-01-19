package io.zeebe.camel;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Zeebe producer.
 */
public class ZeebeProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(ZeebeProducer.class);
    private ZeebeEndpoint endpoint;

    public ZeebeProducer(ZeebeEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody());    
    }

}

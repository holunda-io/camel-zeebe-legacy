package io.zeebe.camel.examle.rnd;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Rnd producer.
 */
public class RndProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(RndProducer.class);
    private RndEndpoint endpoint;

    public RndProducer(RndEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody());    
    }

}

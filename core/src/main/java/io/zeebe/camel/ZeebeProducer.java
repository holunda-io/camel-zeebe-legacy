package io.zeebe.camel;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

import lombok.extern.slf4j.Slf4j;

/**
 * The Zeebe producer.
 */
@Slf4j
public class ZeebeProducer extends DefaultProducer {

    private final ZeebeEndpoint endpoint;

    public ZeebeProducer(final ZeebeEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(final Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody());
    }

}

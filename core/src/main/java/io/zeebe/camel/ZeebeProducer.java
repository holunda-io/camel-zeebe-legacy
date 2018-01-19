package io.zeebe.camel;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Zeebe producer.
 */
@Slf4j
public class ZeebeProducer extends DefaultProducer {

    public ZeebeProducer(ZeebeEndpoint endpoint) {
        super(endpoint);
    }

    @Override
    public void process(final Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody());
    }

}

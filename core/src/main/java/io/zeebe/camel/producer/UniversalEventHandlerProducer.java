package io.zeebe.camel.producer;

import io.zeebe.camel.endpoint.UniversalEventHandlerEndpoint;
import io.zeebe.camel.producer.AbstractZeebeProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;

/**
 * The Zeebe producer.
 *
 * @deprecated since the exhange is in-only, we wont need this producer.
 */
@Slf4j
@Deprecated
public class UniversalEventHandlerProducer extends AbstractZeebeProducer<UniversalEventHandlerEndpoint>
{

    public UniversalEventHandlerProducer(final UniversalEventHandlerEndpoint endpoint) {
        super(endpoint);
    }

    @Override
    public void process(final Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody());
    }

}

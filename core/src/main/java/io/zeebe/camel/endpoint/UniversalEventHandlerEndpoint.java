package io.zeebe.camel.endpoint;

import io.zeebe.camel.ZeebeComponent;
import io.zeebe.camel.consumer.UniversalEventHandlerConsumer;
import io.zeebe.camel.producer.UniversalEventHandlerProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriPath;

import lombok.Data;

/**
 * Represents a Zeebe endpoint.
 */
@Data
@Slf4j
@UriEndpoint(scheme = ZeebeComponent.SCHEME, title = "Zeebe UniversalEventHandler", syntax = "zeebe:universalEventHandler", consumerClass = UniversalEventHandlerConsumer.class, label = "custom")
public class UniversalEventHandlerEndpoint extends AbstractZeebeEndpoint
{
    /**
     * The name.
     */
    @UriPath @Metadata(required = "true")
    private String name;

    /**
     * just to fill the space.
     * TODO: remove
     */
    @UriPath
    @Metadata(required = "true")
    private String option;

    /**
     * The topic to subscribe to.
     */
    @UriPath
    @Metadata(required = "true")
    private String topic;


    public UniversalEventHandlerEndpoint(final EndpointConfiguration configuration, final ZeebeComponent component)
    {
        super(configuration, component);
        log.info("endpoint: {}", this.getClass().getSimpleName());
    }

    @Override
    public Producer createProducer() throws Exception
    {
        return new UniversalEventHandlerProducer(this);
    }

    @Override
    public Consumer createConsumer(final Processor nextProcessorInRoute) throws Exception
    {
        return new UniversalEventHandlerConsumer(this, nextProcessorInRoute);
    }


}

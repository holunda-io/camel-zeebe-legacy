package io.zeebe.camel.handler.universal;

import static io.zeebe.camel.ZeebeComponent.SCHEME;
import static io.zeebe.camel.handler.universal.UniversalEventEndpoint.SYNTAX;
import static io.zeebe.camel.handler.universal.UniversalEventEndpoint.TITLE;

import io.zeebe.camel.AbstractZeebeEndpoint;
import io.zeebe.camel.EndpointConfiguration;
import io.zeebe.camel.ZeebeComponent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriPath;

/**
 * Represents a Zeebe endpoint.
 */
@Data
@Slf4j
@UriEndpoint(scheme = ZeebeComponent.SCHEME, title = TITLE, syntax = SYNTAX, consumerClass = UniversalEventConsumer.class, consumerOnly = true)
public class UniversalEventEndpoint extends AbstractZeebeEndpoint
{

    public static final String OPERATION = "universalEventHandler";
    static final String SYNTAX = SCHEME + ":" + OPERATION;
    static final String TITLE = "Zeebe UniversalEventHandler";

    /**
     * The name.
     */
    @UriPath
    @Metadata(required = "true")
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

    public UniversalEventEndpoint(final EndpointConfiguration configuration)
    {
        super(configuration);
        log.info("endpoint: {}", this.getClass().getSimpleName());
    }

    @Override
    public Consumer createConsumer(final Processor nextProcessorInRoute) throws Exception
    {
        return new UniversalEventConsumer(this, nextProcessorInRoute);
    }

}

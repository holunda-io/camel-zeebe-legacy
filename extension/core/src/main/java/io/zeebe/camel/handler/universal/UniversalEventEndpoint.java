package io.zeebe.camel.handler.universal;

import static io.zeebe.camel.ZeebeComponent.SCHEME;
import static io.zeebe.camel.handler.universal.UniversalEventEndpoint.SYNTAX;
import static io.zeebe.camel.handler.universal.UniversalEventEndpoint.TITLE;

import io.zeebe.camel.ZeebeEndpoint;
import io.zeebe.camel.EndpointConfiguration;
import io.zeebe.camel.ZeebeComponent;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false)
@Slf4j
@UriEndpoint(scheme = ZeebeComponent.SCHEME, title = TITLE, syntax = SYNTAX, consumerClass = UniversalEventConsumer.class, consumerOnly = true)
public class UniversalEventEndpoint extends ZeebeEndpoint
{

    public static final String SUBJECT = "universal-event";
    public static final String OPERATION_SUBSCRIBE = "subscribe";
    public static final String SYNTAX = SCHEME + ":" + SUBJECT;
    public static final String TITLE = "Zeebe UniversalEventHandler";

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

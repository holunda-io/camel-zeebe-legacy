package io.zeebe.camel.handler.task;

import static io.zeebe.camel.ZeebeComponent.SCHEME;
import static io.zeebe.camel.handler.task.TaskEndpoint.SYNTAX;
import static io.zeebe.camel.handler.task.TaskEndpoint.TITLE;

import io.zeebe.camel.AbstractZeebeEndpoint;
import io.zeebe.camel.EndpointConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriPath;

/**
 * Represents a Zeebe endpoint.
 */
@Data
@Slf4j
@UriEndpoint(scheme = SCHEME, title = TITLE, syntax = SYNTAX)
public class TaskEndpoint extends AbstractZeebeEndpoint
{

    public static final String OPERATION = "taskHandler";
    static final String SYNTAX = SCHEME + ":" + OPERATION;
    static final String TITLE = "Zeebe TaskHandler";

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

    public TaskEndpoint(final EndpointConfiguration configuration)
    {
        super(configuration);
        log.info("endpoint: {}", this.getClass().getSimpleName());
    }

    @Override
    public Producer createProducer() throws Exception
    {
        return new DefaultProducer(this)
        {
            @Override
            public void process(final Exchange exchange) throws Exception
            {
                log.info("running ");
            }
        };
    }
}

package io.zeebe.camel.handler.task;

import static io.zeebe.camel.ZeebeComponent.SCHEME;
import static io.zeebe.camel.handler.task.TaskEndpoint.SYNTAX;
import static io.zeebe.camel.handler.task.TaskEndpoint.TITLE;

import io.zeebe.camel.AbstractZeebeEndpoint;
import io.zeebe.camel.EndpointConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
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

    public static final String SUBJECT = "task";
    public static final String OPERATION_CREATE = "create";
    public static final String OPERATION_COMPLETE = "complete";
    public static final String SYNTAX = SCHEME + ":" + SUBJECT;
    public static final String TITLE = "Zeebe TaskHandler";

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
        log.info("endpoint: {} {}", this.getClass().getSimpleName(), configuration);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception
    {
        if (TaskEndpoint.OPERATION_CREATE.equals(configuration.getOperation()))
        {
            return new TaskCreateConsumer(this, processor);
        }
        else
        {
            return null;
        }
    }

    @Override
    public Producer createProducer() throws Exception
    {
        if (TaskEndpoint.OPERATION_COMPLETE.equals(configuration.getOperation()))
        {

            return new TaskProducer(this);
        }
        else
        {
            return null;
        }
    }
}

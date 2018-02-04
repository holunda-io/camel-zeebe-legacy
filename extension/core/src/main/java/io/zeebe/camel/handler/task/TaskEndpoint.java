package io.zeebe.camel.handler.task;

import static io.zeebe.camel.ZeebeComponent.SCHEME;
import static io.zeebe.camel.handler.task.TaskEndpoint.SYNTAX;
import static io.zeebe.camel.handler.task.TaskEndpoint.TITLE;

import io.zeebe.camel.ZeebeEndpoint;
import io.zeebe.camel.EndpointConfiguration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a Zeebe endpoint.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
@UriEndpoint(scheme = SCHEME, title = TITLE, syntax = SYNTAX)
public class TaskEndpoint extends ZeebeEndpoint
{

    public static final String SUBJECT = "task";
    public static final String OPERATION_CREATE = "create";
    public static final String OPERATION_COMPLETE = "complete";
    public static final String SYNTAX = SCHEME + ":" + SUBJECT;
    public static final String TITLE = "Zeebe TaskHandler";

    /**
     * The name.
     */
    @UriPath(name = "subject", description="type of events")
    @Metadata(required = "true")
    private String subject;

    /**
     * just to fill the space.
     */
    @UriPath
    @Metadata(required = "true")
    private String type;

    @UriParam(name = "owner", label = "owner")
    @Metadata(required = "true")
    private String owner;

    public TaskEndpoint(final EndpointConfiguration configuration)
    {
        super(configuration);
        log.info("endpoint: {} {}", this.getClass().getSimpleName(), configuration);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception
    {
        return new TaskCreateConsumer(this, processor);
    }

    @Override
    public Producer createProducer() throws Exception
    {
        return new TaskProducer(this);
    }
}

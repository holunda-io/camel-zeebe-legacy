package io.zeebe.camel.handler.task;

import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zeebe.camel.ZeebeConsumer;
import io.zeebe.camel.api.event.EventHeader;
import io.zeebe.camel.fn.CreateEventHeader;
import io.zeebe.client.TasksClient;
import io.zeebe.client.task.TaskHandler;
import io.zeebe.client.task.TaskSubscription;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Slf4j
public class TaskCreateConsumer extends ZeebeConsumer<TaskEndpoint, TaskHandler, TaskSubscription>
{

    private final TasksClient client;
    private final String topic;
    private final CreateEventHeader createEventHeader = new CreateEventHeader();

    public TaskCreateConsumer(final TaskEndpoint endpoint, Processor processor)
    {
        super(endpoint, processor);

        this.client = endpoint.getClient().tasks();
        this.topic = endpoint.getTopic();
    }

    @Override
    protected TaskHandler createHandler()
    {
        return (client, task) -> {
            try
            {
                EventHeader header = createEventHeader.apply(task.getMetadata(), task.getState());
                ObjectMapper mapper = new ObjectMapper();

                String body = mapper.writeValueAsString(task);
                String p = task.getPayload();
                log.error("payload: {}", p);

                final Exchange exchange = endpoint.createExchange();
                exchange.getIn().setHeaders(header.toMap());
                exchange.getIn().setBody(body);

                getProcessor().process(exchange);
            }
            catch (Exception e)
            {
                // TODO: we have to correctly handle this, exception not allowed in lamdba and completion handled by producer
                throw new RuntimeException("task handling failed", e);
            }
        };
    }

    @Override
    protected TaskSubscription createSubscription(TaskHandler handler)
    {
        return client.newTaskSubscription(topic)
                     .taskType(endpoint.getType())
                     .lockOwner(endpoint.getOwner())
                     .lockTime(Duration.ofSeconds(10))
                     .handler(createHandler())
                     .open();
    }
}

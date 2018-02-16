package io.zeebe.camel.handler.task;

import java.time.Duration;

import io.zeebe.camel.ZeebeConsumer;
import io.zeebe.camel.processor.TaskEventToJsonProcessor;
import io.zeebe.client.TasksClient;
import io.zeebe.client.event.TaskEvent;
import io.zeebe.client.task.TaskHandler;
import io.zeebe.client.task.TaskSubscription;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Consumer that subscribes a worker identified by {@link TaskUri#lockOwner(String)} to the given topic
 * and taskType.
 * <p>
 * {@link io.zeebe.client.event.TaskEvent}s that are received from zeebe are transformed to json and forwarded to
 * the next processor in route.
 */
@Slf4j
public class TaskCreateConsumer extends ZeebeConsumer<TaskEndpoint, TaskHandler, TaskSubscription>
{
    private final TasksClient client;
    private final String topic;
    private final TaskEventToJsonProcessor taskEventToJsonProcessor = new TaskEventToJsonProcessor();

    public TaskCreateConsumer(final TaskEndpoint endpoint, Processor processor)
    {
        super(endpoint, processor);

        this.client = endpoint.getClient().tasks();
        this.topic = endpoint.getTopic();
    }

    @Override
    protected TaskHandler createHandler()
    {
        return new TaskHandler()
        {
            @Override
            @SneakyThrows
            public void handle(TasksClient client, TaskEvent task)
            {
                final Exchange exchange = endpoint.createExchange();
                exchange.getIn().setBody(task);
                taskEventToJsonProcessor.process(exchange);

                getProcessor().process(exchange);
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

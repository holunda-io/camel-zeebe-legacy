package io.zeebe.camel.handler.task;

import java.time.Duration;

import io.zeebe.camel.ZeebeConsumer;
import io.zeebe.client.TasksClient;
import io.zeebe.client.task.TaskHandler;
import io.zeebe.client.task.TaskSubscription;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Processor;

@Slf4j
public class TaskCreateConsumer extends ZeebeConsumer<TaskEndpoint, TaskHandler, TaskSubscription>
{

    private final TasksClient client;
    private final String topic;

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
                getProcessor().process(createExchangeForEvent.apply(task));
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
                     .lockOwner("foo")
                     .lockTime(Duration.ofSeconds(10))
                     .taskType("doSomething")
                     .handler(createHandler())
                     .open();
    }
}

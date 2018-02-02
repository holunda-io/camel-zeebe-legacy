package io.zeebe.camel.handler.task;

import java.time.Duration;

import io.zeebe.camel.AbstractZeebeConsumer;
import io.zeebe.camel.fn.SubscriptionAdapter;
import io.zeebe.client.task.TaskHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Processor;

@Slf4j
public class TaskCreateConsumer extends AbstractZeebeConsumer<TaskEndpoint, TaskHandler>
{
    public TaskCreateConsumer(final TaskEndpoint endpoint, Processor processor)
    {
        super(endpoint, processor);
        log.error("____________ HERE!!!!!");
    }

    @Override
    protected TaskHandler createHandler(final Processor processor)
    {
        return (client, task) -> {
            try
            {
                processor.process(createExchangeForEvent.apply(task));
            }
            catch (Exception e)
            {
                // TODO: we have to correctly handle this, exception not allowed in lamdba and completion handled by producer
                throw new RuntimeException("task handling failed", e);
            }
        };
    }

    @Override
    protected SubscriptionAdapter createSubscription(TaskHandler handler)
    {
        return SubscriptionAdapter.of(endpoint.getClient()
                                              .tasks()
                                              .newTaskSubscription("default-topic")
                                              .lockOwner("foo")
                                              .lockTime(Duration.ofSeconds(10))
                                              .taskType("doSomething")
                                              .handler(createHandler(getProcessor()))
                                              .open());
    }
}

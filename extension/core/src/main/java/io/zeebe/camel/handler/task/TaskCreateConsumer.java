package io.zeebe.camel.handler.task;

import io.zeebe.camel.ZeebeConsumer;
import io.zeebe.camel.processor.TaskEventToJsonProcessor;
import io.zeebe.client.api.clients.JobClient;
import io.zeebe.client.api.events.JobEvent;
import io.zeebe.client.api.subscription.JobHandler;
import io.zeebe.client.api.subscription.JobWorker;
import io.zeebe.spring.api.SpringZeebeApiKt;
import io.zeebe.spring.api.command.CreateJobWorker;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Consumer that subscribes a worker identified by {@link TaskUri#lockOwner(String)} to the given
 * topic and taskType.
 * <p>
 * {@link io.zeebe.camel.api.event.JobEvent}s that are received from zeebe are transformed to json and
 * forwarded to the next processor in route.
 */
@Slf4j
public class TaskCreateConsumer extends ZeebeConsumer<TaskEndpoint, JobHandler, JobWorker> {

    private final String topic;
    private final TaskEventToJsonProcessor taskEventToJsonProcessor = new TaskEventToJsonProcessor();

    public TaskCreateConsumer(final TaskEndpoint endpoint, Processor processor) {
        super(endpoint, processor);

        this.topic = endpoint.getTopic();
    }

    @Override
    protected JobHandler createHandler() {
        return new JobHandler() {
            @SneakyThrows
            @Override
            public void handle(JobClient client, JobEvent jobEvent) {

                final Exchange exchange = endpoint.createExchange();
                exchange.getIn().setBody(jobEvent);
                taskEventToJsonProcessor.process(exchange);

                getProcessor().process(exchange);
            }
        };
    }

    @Override
    protected JobWorker createSubscription(JobHandler handler) {
        CreateJobWorker createJobWorker = new CreateJobWorker(topic, endpoint.getType(), handler);
        return SpringZeebeApiKt.apply(getClient(), createJobWorker);
    }
}

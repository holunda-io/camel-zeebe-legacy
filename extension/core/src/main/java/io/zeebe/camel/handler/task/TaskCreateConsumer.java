package io.zeebe.camel.handler.task;

import io.zeebe.broker.job.processor.JobSubscription;
import io.zeebe.camel.ZeebeConsumer;
import io.zeebe.camel.processor.TaskEventToJsonProcessor;
import io.zeebe.client.api.clients.JobClient;
import io.zeebe.client.api.subscription.JobHandler;
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
public class TaskCreateConsumer extends ZeebeConsumer<TaskEndpoint, JobHandler, JobSubscription> {

    private final JobClient client;
    private final String topic;
    private final TaskEventToJsonProcessor taskEventToJsonProcessor = new TaskEventToJsonProcessor();

    public TaskCreateConsumer(final TaskEndpoint endpoint, Processor processor) {
        super(endpoint, processor);

        this.client = null;// FIXME: implement endpoint.getClient().;
        this.topic = null; // FIXME: implementendpoint.getTopic();
    }

    @Override
    protected JobHandler createHandler() {
        return new JobHandler() {
            @SneakyThrows
            @Override
            public void handle(JobClient client, io.zeebe.client.api.events.JobEvent jobEvent) {

                final Exchange exchange = endpoint.createExchange();
                exchange.getIn().setBody(jobEvent);
                taskEventToJsonProcessor.process(exchange);

                getProcessor().process(exchange);
            }
        };
    }

    @Override
    protected JobSubscription createSubscription(JobHandler handler) {
        return null;
        // FIXME: implement
//        client.newWorker().jobType()newTaskSubscription(topic)
//            .taskType(endpoint.getType())
//            .lockOwner(endpoint.getOwner())
//            .lockTime(Duration.ofSeconds(10))
//            .handler(createHandler())
//            .open();
    }
}

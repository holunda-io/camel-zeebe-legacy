package io.zeebe.camel;

import java.time.Duration;
import java.util.Date;

import io.zeebe.camel.fn.CamelTaskHandler;
import io.zeebe.client.ZeebeClient;
import io.zeebe.client.event.WorkflowInstanceEvent;
import io.zeebe.test.ZeebeTestRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@Slf4j
public class ZeebeWorksSpike
{
    @Rule
    public final ZeebeTestRule zeebe = new ZeebeTestRule();

    private final CamelContext context = new DefaultCamelContext();

    private ZeebeClient client;
    private String topic;

    @Before
    public void setUp() throws Exception
    {
        this.client = zeebe.getClient();

        this.topic = zeebe.getDefaultTopic();

        client.workflows().deploy(topic)
              .addResourceFromClasspath("dummy.bpmn")
              .execute();
    }

    @After
    public void tearDown() throws Exception
    {
        context.stop();
    }

    @Test
    public void name()
    {
        final CamelTaskHandler handler = CamelTaskHandler.of(s -> "{\"bar\": \"foo\"}");

        final WorkflowInstanceEvent workflowInstance = client.workflows().create(topic)
                                                             .bpmnProcessId("process_dummy")
                                                             .payload("{\"foo\":\"hello\"}")
                                                             .latestVersion()
                                                             .execute();

        client.tasks().newTaskSubscription(topic)
              .taskType("doSomething")
              .lockOwner("test")
              .lockTime(Duration.ofSeconds(30))
              .handler(handler)
              .open();

        zeebe.waitUntilWorkflowInstanceCompleted(workflowInstance.getWorkflowInstanceKey());
    }

    @Test
    public void name1() throws Exception
    {
        context.addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from("direct:foo").to("log:message");

                from("timer:simple").transform()
                                    .exchange(e -> e.getProperty(Exchange.TIMER_FIRED_TIME, Date.class).getTime())
                                    .to("direct:foo");
            }
        });

        context.start();

        Thread.sleep(5000L);
    }
}

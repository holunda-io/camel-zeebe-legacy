package io.zeebe.camel;

import io.zeebe.camel.helper.Steps;
import io.zeebe.test.ZeebeTestRule;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SubscribeToGeneralEventsOnTopicTest
{
    @Rule
    public final ZeebeTestRule zeebe = new ZeebeTestRule();

    private Steps steps;

    @Before
    public void setUp() {
        steps = new Steps(zeebe);
    }

    @After
    public void tearDown() throws Exception
    {
        steps.getContext().stop();
    }

    @Test
    public void subscribe_and_consume_generalEvents() throws Exception
    {
        steps.getContext().addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from("zeebe:universal-event:subscribe?option=hello").to("direct:foo");

                from("direct:foo").to("log:message");
            }
        });

        steps.getContext().start();

        steps.deploy();
        steps.startProcess();

        Thread.sleep(2000L);
    }

}

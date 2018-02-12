package io.zeebe.camel.handler.universal;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.zeebe.camel.helper.Steps;
import io.zeebe.test.ZeebeTestRule;

public class UniversalEventConsumerITest
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
        final MockEndpoint mock = steps.mockEndpoint("mock:receive");

        // expect two messages (create/created)
        mock.expectedMessageCount(2);
        mock.expectedHeaderReceived("eventType", "WORKFLOW");

        steps.getContext().addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from(UniversalEventUri.topic("default-topic").get())
                    .to("log:message")
                    .to("mock:receive");
            }
        });

        steps.getContext().start();
        steps.deploy();

        // 2 messages received
        mock.assertIsSatisfied();
    }

    @Test
    public void marshal_to_json() throws Exception {
        final MockEndpoint mock = steps.mockEndpoint("mock:receive");

        steps.getContext().addRoutes(UniversalEventUri.topic("default-topic")
            .route()
            //.toJson()
            .logger("universal-handler")
            .to("direct:fire-and-forget")
        );

        steps.getContext().addRoutes(new RouteBuilder() {
                                         @Override
                                         public void configure() throws Exception {
                                             from("direct:fire-and-forget")
                                                 .to(mock);
                                         }
                                     }
        );

        steps.getContext().start();
        steps.deploy();
    }
}

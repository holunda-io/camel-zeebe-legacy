package io.zeebe.camel.handler.universal;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Rule;
import org.junit.Test;

import io.zeebe.camel.test.CamelZeebeRule;
import io.zeebe.camel.test.CamelZeebeTest;

public class UniversalEventConsumerRuleTest
{
    @Rule
    public final CamelZeebeRule zeebe = new CamelZeebeRule(this);

    @Test
    @CamelZeebeTest(routeBuilder = "route", mockEndpoint = "mock:receive")
    public void subscribe_and_consume_generalEvents() throws Exception
    {
        final MockEndpoint mock = zeebe.getMockEndpoint();

        // expect two messages (create/created)
        mock.expectedMessageCount(2);
        mock.expectedHeaderReceived("eventType", "WORKFLOW");

        zeebe.deploy("dummy.bpmn");

        // 2 messages received
        mock.assertIsSatisfied();
    }

    private RouteBuilder route() {
        return new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from(UniversalEventUri.topic("default-topic").get())
                    .to("log:message")
                    .to("mock:receive");
            }
        };
    }

}

package io.zeebe.camel.handler.universal;

import io.zeebe.camel.test.CamelZeebeRule;
import io.zeebe.camel.test.CamelZeebeTest;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Rule;
import org.junit.Test;

public class UniversalEventConsumerRuleTest
{
    @Rule
    public final CamelZeebeRule zeebe = new CamelZeebeRule(this);

    @Test
    @CamelZeebeTest(routeBuilder = "route", mockEndpoint = "mock:receive")
    public void subscribe_and_consume_generalEvents() throws Exception
    {
        // expect two messages (create/created)
        zeebe.mockEndpoint().expectedMessageCount(2);
        zeebe.mockEndpoint().expectedHeaderReceived("type", "WORKFLOW");
        zeebe.mockEndpoint().expectedHeaderValuesReceivedInAnyOrder("state", "CREATE", "CREATED");

        zeebe.deploy("dummy.bpmn");

        // 2 messages received
        zeebe.mockEndpoint().assertIsSatisfied();
    }

    private RouteBuilder route()
    {
        return new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from(UniversalEventUri.topic("default-topic").get()).to("log:message").to("mock:receive");
            }
        };
    }

}

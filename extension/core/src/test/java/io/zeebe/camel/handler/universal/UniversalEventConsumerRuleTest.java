package io.zeebe.camel.handler.universal;

import static io.zeebe.camel.TestConstants.DUMMY_BPMN;

import io.zeebe.camel.test.CamelZeebeRule;
import io.zeebe.camel.test.CamelZeebeTest;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Rule;
import org.junit.Test;

public class UniversalEventConsumerRuleTest {

    @Rule
    public final CamelZeebeRule zeebe = new CamelZeebeRule(this);

    private final RouteBuilder route = new RouteBuilder() {
        @Override
        public void configure() {
            from(UniversalEventUri.topic(zeebe.getDefaultTopic()).get())
                .to("log:message?showHeaders=true&multiline=true")
                .to("mock:receive");
        }
    };

    @Test
    @CamelZeebeTest(routeBuilder = "route", mockEndpoint = "mock:receive")
    public void subscribe_and_consume_generalEvents() throws Exception {
        // expect two messages (create/created)
        zeebe.mockEndpoint().expectedMessageCount(1);
        zeebe.mockEndpoint().expectedHeaderReceived("type", "WORKFLOW");
        zeebe.mockEndpoint().expectedHeaderValuesReceivedInAnyOrder("state", "CREATE", "CREATED");

        zeebe.deploy(DUMMY_BPMN);

        // 2 messages received
        zeebe.mockEndpoint().assertIsSatisfied();
    }

}

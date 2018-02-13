package io.zeebe.camel.handler.task;

import static io.zeebe.camel.TestConstants.*;

import io.zeebe.camel.TestConstants;
import io.zeebe.camel.test.CamelZeebeRule;
import io.zeebe.camel.test.CamelZeebeTest;
import lombok.SneakyThrows;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Rule;
import org.junit.Test;


public class TaskHandlerTest
{

    @Rule
    public final CamelZeebeRule zeebe = new CamelZeebeRule(this);

    private final RouteBuilder createTaskToMock = new RouteBuilder()
    {
        @Override
        public void configure() throws Exception
        {
            from(TaskUri.topic(zeebe.getDefaultTopic())
                        .lockOwner("test")
                        .type(DUMMY_TASK)
                        .get())
            .to("log:message?showHeaders=true&multiline=true")
            .to("mock:receive");
        }
    };

    @Test
    @CamelZeebeTest(mockEndpoint = "mock:receive", routeBuilder = "createTaskToMock")
    public void a_created_task_is_published_as_json() throws Exception
    {
        zeebe.mockEndpoint().expectedHeaderReceived("state", "LOCKED");
        zeebe.deploy(DUMMY_BPMN);
        zeebe.startProcess(DUMMY_KEY, FOO_PAYLOAD);

        zeebe.mockEndpoint().assertIsSatisfied();
    }

}

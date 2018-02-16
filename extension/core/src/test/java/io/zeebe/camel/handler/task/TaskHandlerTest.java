package io.zeebe.camel.handler.task;

import static io.zeebe.camel.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import io.zeebe.camel.api.command.CompleteTaskCommand;
import io.zeebe.camel.api.processor.TaskWorkerProcessor;
import io.zeebe.camel.handler.universal.UniversalEventUri;
import io.zeebe.camel.test.CamelZeebeRule;
import io.zeebe.camel.test.CamelZeebeTest;
import io.zeebe.camel.test.JsonBodyPredicate;
import io.zeebe.client.event.WorkflowInstanceEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Rule;
import org.junit.Test;

@Slf4j
public class TaskHandlerTest
{

    private static final String LOG_MESSAGE = "log:message?showHeaders=true&multiline=true";

    @Rule
    public final CamelZeebeRule zeebe = new CamelZeebeRule(this);

    private final RouteBuilder createTaskToMock = new RouteBuilder()
    {
        @Override
        public void configure() throws Exception
        {
            from(TaskUri.topic(zeebe.getDefaultTopic()).lockOwner("test").type(DUMMY_TASK).get()).to(LOG_MESSAGE).to("mock:receive");
        }
    };

    @Test
    @CamelZeebeTest(mockEndpoint = "mock:receive", routeBuilder = "createTaskToMock")
    public void a_created_task_is_published_as_json() throws Exception
    {
        zeebe.mockEndpoint().expectedHeaderReceived("state", "LOCKED");
        zeebe.mockEndpoint().expectedMessageCount(1);
        zeebe.mockEndpoint().expectedMessagesMatches(new JsonBodyPredicate()
        {
            @Override
            public void matches(final JsonNode json)
            {
                assertThat(json.at("/payloadSource").asText()).as("payload did not match!").isEqualTo(BAR_PAYLOAD);
                assertThat(json.at("/headers/activityId").asText()).isEqualTo("task_doSomething");
            }
        });
        zeebe.deploy(DUMMY_BPMN);
        zeebe.startProcess(DUMMY_KEY, FOO_PAYLOAD);

        zeebe.mockEndpoint().assertIsSatisfied();
    }

    private final RouteBuilder routesForTaskCompletion = new RouteBuilder()
    {
        @Override
        public void configure() throws Exception
        {
            // create task and notify worker
            from(TaskUri.topic(zeebe.getDefaultTopic()).lockOwner("test").type(DUMMY_TASK).get()).to("direct:notifyExternalWorker");

            // worker receives, works on task and sends complete command
            from("direct:notifyExternalWorker").process(
                TaskWorkerProcessor.processor(task -> CompleteTaskCommand.builder().task(task).payload("\"bar\":\"world\"").build()))
                                               .to("direct:completeTask");

            // complete command is received and the task is completed
            from("direct:completeTask").to(TaskUri.topic(zeebe.getDefaultTopic()).type(DUMMY_TASK).lockOwner("test").get());

            // let only the wfi-completed event pass to mockEndpoint
            from(UniversalEventUri.topic(zeebe.getDefaultTopic()).get())
                .filter(header("state").isEqualTo("WORKFLOW_INSTANCE_COMPLETED"))
                .to(LOG_MESSAGE).to("mock:receive");
        }
    };

    @Test
    @CamelZeebeTest(routeBuilder = "routesForTaskCompletion", mockEndpoint = "mock:receive")
    public void an_external_worker_can_complete_a_task() throws Exception
    {
        // expect only one wfi-completed event
        zeebe.mockEndpoint().expectedMessageCount(1);

        zeebe.deploy(DUMMY_BPMN);
        zeebe.startProcess(DUMMY_KEY, FOO_PAYLOAD);
        // route processing completes task, see above

        // the test is green when we received a wfi-completed event
        zeebe.mockEndpoint().assertIsSatisfied();
    }

}

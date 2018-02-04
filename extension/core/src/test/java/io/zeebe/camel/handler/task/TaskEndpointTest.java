package io.zeebe.camel.handler.task;

import static io.zeebe.camel.ZeebeComponent.DEFAULT_TOPIC;

import io.zeebe.camel.api.command.CompleteTaskCommand;
import io.zeebe.camel.handler.universal.UniversalEventUri;
import io.zeebe.camel.helper.Steps;
import io.zeebe.test.ZeebeTestRule;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class TaskEndpointTest
{

    public static abstract class Setup
    {

        @Rule
        public final ZeebeTestRule zeebe = new ZeebeTestRule();

        protected Steps steps;

        @Before
        public void setUp()
        {
            steps = new Steps(zeebe);
        }

        @After
        public void tearDown() throws Exception
        {
            steps.getContext().stop();
        }
    }

    public static class CompleteTask extends Setup
    {

        @Test
        public void start_and_complete_task() throws Exception
        {
            MockEndpoint mock = steps.mockEndpoint("mock:received");

            steps.getContext().addRoutes(new RouteBuilder()
            {
                @Override
                public void configure() throws Exception
                {

                    // subscribes to tasks on zeebeClient and forwards to jms
                    from(TaskUri.topic(DEFAULT_TOPIC).type("doSomething").lockOwner("remote").get())
                        .convertBodyTo(io.zeebe.camel.api.event.TaskEvent.class)
                        .to("direct:someMessageSystemCreate");

                    from("direct:someMessageSystemCreate")

                        .process(exchange -> {
                            io.zeebe.camel.api.event.TaskEvent taskEvent = exchange.getIn().getBody(io.zeebe.camel.api.event.TaskEvent.class);
                            final CompleteTaskCommand completeTaskCommand = CompleteTaskCommand.builder()
                                                                                               .task(taskEvent)
                                                                                               .payload("{\"bar\":\"hello\"}")
                                                                                               .build();

                            exchange.getIn().setBody(completeTaskCommand, CompleteTaskCommand.class);
                        }).to("direct:someMessageSystemComplete");

                    // reads from jms and sends completeTaskCommand to producer
                    from("direct:someMessageSystemComplete").to("log:message").to(TaskUri.topic(DEFAULT_TOPIC).type("doSomething").lockOwner("remote").get());

                  from(UniversalEventUri.topic(DEFAULT_TOPIC).name("log").get()).to("log:message");
                }
            });

            //            AtomicReference<TaskEvent> task = new AtomicReference<>();

            steps.getContext().start();
            steps.deploy();
            steps.startProcess();

            //            await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            //
            //                assertThat(task.get()).isNotNull()
            //
            //            );
            //
            //            // FIXME: replace with endpoint
            //            //steps.getZeebe().getClient().tasks().complete(task.get()).payload("{\"bar\":\"hello\"}").execute();
            //
            //
            //
            //            io.zeebe.camel.api.TaskEvent taskEvent = TaskTypeConverter.convert(task.get());
            ////
            ////
            ////
            ////            // sent from remote client
            ////            final CompleteTaskCommand completeTaskCommand = CompleteTaskCommand.builder()
            ////                                                                               .task(taskEvent)
            ////                                                                               .payload("{\"bar\":\"hello\"}")
            ////                                                                               .build();
            //            steps.getContext().createProducerTemplate().sendBody("direct:someMessageSystem", completeTaskCommand);
            //
            //            subscription.close();
            //
            //            steps.getZeebe().waitUntilTaskCompleted(task.get().getMetadata().getKey());
            //        }

            Thread.sleep(10000);
        }

    }
}

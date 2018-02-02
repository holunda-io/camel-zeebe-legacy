package io.zeebe.camel;

import io.zeebe.camel.api.CompleteTaskCommand;
import io.zeebe.camel.handler.task.TaskTypeConverter;
import io.zeebe.camel.helper.Steps;
import io.zeebe.client.event.TaskEvent;
import io.zeebe.test.ZeebeTestRule;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
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
            steps.getContext().addRoutes(new RouteBuilder()
            {
                @Override
                public void configure() throws Exception
                {

                    // subscribes to tasks on zeebeClient and forwards to jms
                    from("zeebe:task:create?option=foo")
                        // TODO: here (or in consumer directly) we must convert from zeebe-TaskEvent to Camel-TaskEvent
                        .to("direct:someMessageSystemCreate");

                    from("direct:someMessageSystemCreate").process(new Processor()
                    {
                        @Override
                        public void process(Exchange exchange) throws Exception
                        {
                            io.zeebe.camel.api.TaskEvent taskEvent = TaskTypeConverter.convert(exchange.getIn().getBody(TaskEvent.class));
                            final CompleteTaskCommand completeTaskCommand = CompleteTaskCommand.builder()
                                                                                               .task(taskEvent)
                                                                                               .payload("{\"bar\":\"hello\"}")
                                                                                               .build();

                            exchange.getIn().setBody(completeTaskCommand, CompleteTaskCommand.class);
                        }
                    }).to("direct:someMessageSystemComplete");

                    // reads from jms and sends completeTaskCommand to producer
                    from("direct:someMessageSystemComplete").to("log:message").to("zeebe:task:complete");

                    from("zeebe:universal-event:subscribe").to("log:message");
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

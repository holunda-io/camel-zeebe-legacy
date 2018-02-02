package io.zeebe.camel;

import io.zeebe.camel.helper.Steps;
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

    static abstract class Setup {

        @Rule
        public final ZeebeTestRule zeebe = new ZeebeTestRule();

        protected Steps steps;

        @Before
        public void setUp() {
            steps = new Steps(zeebe);
        }

        @After
        public void tearDown() throws Exception
        {
            steps.getContext().stop();
        }
    }

    public static class CompleteTask extends Setup {

        @Test
        public void start_and_complete_task() throws Exception
        {
            steps.getContext().addRoutes(new RouteBuilder()
            {
                @Override
                public void configure() throws Exception
                {
                    from("direct:foo")
                        .process(new Processor()
                        {
                            @Override
                            public void process(Exchange exchange) throws Exception
                            {

                            }
                        });
                }
            });

            steps.deploy();

        }

    }
}

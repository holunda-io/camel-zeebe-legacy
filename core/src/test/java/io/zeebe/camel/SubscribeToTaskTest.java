package io.zeebe.camel;

import io.zeebe.camel.helper.Steps;
import io.zeebe.test.ZeebeTestRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@Slf4j
public class SubscribeToTaskTest
{
    @Rule
    public final ZeebeTestRule zeebe = new ZeebeTestRule();

    private final Steps steps = new Steps(zeebe);



    @Before
    public void setUp() throws Exception
    {
        steps.getContext().addComponent("zeebe", new ZeebeComponent(zeebe.getClient()));


    }

    @Test
    public void consume_from_client() throws Exception
    {
        steps.getContext().addRoutes(new RouteBuilder()
        {
            @Override
            public void configure() throws Exception
            {
                from("zeebe:foo").to("stream:out");
            }
        });

        steps.getContext().start();


        steps.deploy();
        steps.startProcess();


    }

    @After
    public void tearDown() throws Exception
    {
        steps.getContext().stop();
    }

}

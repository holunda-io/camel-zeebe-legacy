package io.zeebe.camel.test;

import java.lang.reflect.Method;
import java.util.function.Supplier;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.event.WorkflowInstanceEvent;
import io.zeebe.test.ZeebeTestRule;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class CamelZeebeRule extends ZeebeTestRule
{

    private final Object test;
    private final Class<? extends Component> componentClass;
    private final CamelContext camelContext;
    private final Supplier<ZeebeClient> clientSupplier;

    private MockEndpoint mockEndpoint;

    public CamelZeebeRule(final Object test)
    {
        this(test, new DefaultCamelContext(), "io.zeebe.camel.ZeebeComponent");
    }

    @SneakyThrows
    public CamelZeebeRule(final Object test, CamelContext camelContext, final String componentFQN)
    {
        this.test = test;

        this.componentClass = (Class<? extends Component>) Class.forName(componentFQN);
        this.camelContext = camelContext;

        this.clientSupplier = () -> getClient();
    }

    @Override
    @SneakyThrows
    public Statement apply(final Statement base, final Description description)
    {
        final CamelZeebeTest annotation = description.getAnnotation(CamelZeebeTest.class);

        Method method = description.getTestClass().getDeclaredMethod(annotation.routeBuilder());
        method.setAccessible(true);
        RouteBuilder routeBuilder = (RouteBuilder) method.invoke(test);


        if (!"".equals(annotation.mockEndpoint()))
        {
            mockEndpoint = camelContext.getEndpoint(annotation.mockEndpoint(), MockEndpoint.class);
        }

        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                before();
                try
                {
                    camelContext.addRoutes(routeBuilder);
                    camelContext.start();

                    base.evaluate();
                }
                finally
                {
                    after();
                }
            }
        };
    }

    @Override
    protected void before()
    {
        super.before();

        registerComponent();

    }

    @Override
    protected void after()
    {
        super.after();
        try
        {
            camelContext.stop();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public CamelContext getCamelContext()
    {
        return camelContext;
    }

    public void deploy(String process)
    {
        getClient().workflows().deploy(getDefaultTopic()).addResourceFromClasspath(process).execute();
    }

    public void startProcess(String processKey, String payload)
    {

        final WorkflowInstanceEvent workflowInstance = getClient().workflows()
                                                                  .create(getDefaultTopic())
                                                                  .bpmnProcessId(processKey)
                                                                  .payload(payload)
                                                                  .latestVersion()
                                                                  .execute();

    }

    public MockEndpoint mockEndpoint()
    {
        return mockEndpoint;
    }

    @SneakyThrows
    Component registerComponent()
    {
        String scheme = (String) componentClass.getDeclaredField("SCHEME").get(null);
        Component component = componentClass.getConstructor(Supplier.class).newInstance(clientSupplier);
        camelContext.addComponent(scheme, component);

        return component;
    }

    Class<? extends Component> getComponentClass()
    {
        return componentClass;
    }
}

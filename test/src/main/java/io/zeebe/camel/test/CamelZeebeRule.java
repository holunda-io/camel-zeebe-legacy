package io.zeebe.camel.test;

import java.lang.reflect.Method;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.event.WorkflowInstanceEvent;
import io.zeebe.test.ZeebeTestRule;
import lombok.SneakyThrows;

public class CamelZeebeRule extends ZeebeTestRule {

    private final Object test;
    private final String componentFQN;
    private final CamelContext camelContext = new DefaultCamelContext();

    private MockEndpoint mockEndpoint;

    public CamelZeebeRule(final Object test) {
        this(test, "io.zeebe.camel.ZeebeComponent");
    }

    public CamelZeebeRule(final Object test, String componentFQN) {
        this.test = test;

        this.componentFQN = componentFQN;
    }

    @Override
    @SneakyThrows
    public Statement apply(final Statement base, final Description description) {
        try {
            final Class<?> zeebe = Class.forName(componentFQN);
            String scheme = (String) zeebe.getDeclaredField("SCHEME").get(null);
            Component zeebeComponent = (Component) zeebe.getConstructor(ZeebeClient.class).newInstance(getClient());

            camelContext.addComponent(scheme, zeebeComponent);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        CamelZeebeTest annotation = description.getAnnotation(CamelZeebeTest.class);

        Method method = description.getTestClass().getDeclaredMethod(annotation.routeBuilder());
        method.setAccessible(true);
        RouteBuilder routeBuilder = (RouteBuilder) method.invoke(test);

        if(!"".equals(annotation.mockEndpoint())) {
            camelContext.getEndpoint(annotation.mockEndpoint(), MockEndpoint.class);
        }

        camelContext.addRoutes(routeBuilder);
        camelContext.start();

        return super.apply(base, description);
    }

    @Override
    protected void before() {
        super.before();
    }

    @Override
    protected void after() {
        super.after();
        try {
            camelContext.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CamelContext getCamelContext() {
        return camelContext;
    }

    public void deploy(String process) {
        getClient().workflows().deploy(getDefaultTopic())
            .addResourceFromClasspath(process)
            .execute();
    }

    public void startProcess(String processKey, String payload) {

        final WorkflowInstanceEvent workflowInstance = getClient().workflows().create(getDefaultTopic())
            .bpmnProcessId(processKey)
            .payload(payload)
            .latestVersion()
            .execute();

    }

    public MockEndpoint getMockEndpoint() {
        return mockEndpoint;
    }
}

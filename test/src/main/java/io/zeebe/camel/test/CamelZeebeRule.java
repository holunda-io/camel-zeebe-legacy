package io.zeebe.camel.test;

import io.zeebe.client.ZeebeClient;
import io.zeebe.test.TopicEventRecorder;
import io.zeebe.test.ZeebeTestRule;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class CamelZeebeRule extends ZeebeTestRule {

    private final Object test;
    private final Class<? extends Component> componentClass;
    private final CamelContext camelContext;
    private final Supplier<ZeebeClient> clientSupplier;

    private MockEndpoint mockEndpoint;

    public CamelZeebeRule(final Object test) {
        this(test, new DefaultCamelContext(), "io.zeebe.camel.ZeebeComponent");
    }

    @SneakyThrows
    public CamelZeebeRule(final Object test, CamelContext camelContext, final String componentFQN) {
        this.test = test;

        this.componentClass = (Class<? extends Component>) Class.forName(componentFQN);
        this.camelContext = camelContext;

        this.clientSupplier = () -> getClient();
    }

    @Override
    @SneakyThrows
    public Statement apply(final Statement base, final Description description) {
        final CamelZeebeTest annotation = description.getAnnotation(CamelZeebeTest.class);

        if (!"".equals(annotation.mockEndpoint())) {
            mockEndpoint = camelContext.getEndpoint(annotation.mockEndpoint(), MockEndpoint.class);
        }

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before();
                try {
                    for (RouteBuilder r : routeBuilderFromAnnotation(annotation, description,
                        test)) {
                        camelContext.addRoutes(r);
                    }
                    camelContext.start();

                    base.evaluate();
                } finally {
                    after();
                }
            }
        };
    }

    static List<RouteBuilder> routeBuilderFromAnnotation(CamelZeebeTest annotation,
        Description description, Object test) {
        List<RouteBuilder> rbs = new ArrayList<>();
        if (!"".equals(annotation.routeBuilder())) {
            rbs.add(
                routeBuilderFromField(annotation.routeBuilder(), description.getTestClass(), test));
        }
        if (!"".equals(annotation.routeBuilderSupplier())) {
            rbs.add(routeBuilderFromMethod(annotation.routeBuilderSupplier(),
                description.getTestClass(), test));
        }

        return rbs;
    }

    @SneakyThrows
    static RouteBuilder routeBuilderFromMethod(String methodName, Class<?> testClass,
        Object testInstance) {
        Method method = testClass.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return (RouteBuilder) method.invoke(testInstance);
    }

    @SneakyThrows
    static RouteBuilder routeBuilderFromField(String fieldName, Class<?> testClass,
        Object testInstance) {
        Field field = testClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (RouteBuilder) field.get(testInstance);
    }

    @Override
    protected void before() {
        super.before();

        registerComponent();

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
        getClient()
            .topicClient(getDefaultTopic())
            .workflowClient()
            .newDeployCommand()
            .addResourceFromClasspath(process)
            .send()
            .join();
    }

    public void startProcess(String processKey, String payload) {
        getClient()
            .topicClient()
            .workflowClient()
            .newCreateInstanceCommand()
            .bpmnProcessId(processKey)
            .latestVersion()
            .payload(payload)
            .send()
            .join();
    }

    public MockEndpoint mockEndpoint() {
        return mockEndpoint;
    }

    @SneakyThrows
    Component registerComponent() {
        String scheme = (String) componentClass.getDeclaredField("SCHEME").get(null);
        Component component = componentClass.getConstructor(Supplier.class)
            .newInstance(clientSupplier);
        camelContext.addComponent(scheme, component);

        return component;
    }

    Class<? extends Component> getComponentClass() {
        return componentClass;
    }

    @SneakyThrows
    public TopicEventRecorder getTopicEventRecorder() {
        Field field = ZeebeTestRule.class.getDeclaredField("topicEventRecorder");
        field.setAccessible(true);

        return (TopicEventRecorder) field.get(this);
    }

}

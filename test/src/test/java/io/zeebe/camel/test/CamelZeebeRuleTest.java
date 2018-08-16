package io.zeebe.camel.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.zeebe.client.ZeebeClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

@Slf4j
public class CamelZeebeRuleTest {

    private final Object test = mock(Object.class);
    private final ZeebeClient client = mock(ZeebeClient.class);
    private final CamelContext camelContext = mock(CamelContext.class);

    @Test
    public void resolve_component_class() {
        final CamelZeebeRule rule = new CamelZeebeRule(test, camelContext, DummyComponent.FQN);

        assertThat(rule.getComponentClass()).isEqualTo(DummyComponent.class);
    }

    @Test
    public void register_component_instance() {
        final CamelZeebeRule rule = Mockito
            .spy(new CamelZeebeRule(test, camelContext, DummyComponent.FQN));
        doReturn(client).when(rule).getClient();

        ArgumentCaptor<String> schemeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Component> componentCaptor = ArgumentCaptor.forClass(Component.class);

        Component component = rule.registerComponent();

        assertThat(component).isNotNull();
        assertThat(component).isInstanceOf(DummyComponent.class);

        verify(camelContext).addComponent(schemeCaptor.capture(), componentCaptor.capture());

        assertThat(schemeCaptor.getValue()).isEqualTo(DummyComponent.SCHEME);
        assertThat(componentCaptor.getValue()).isEqualTo(component);
    }

    @Test
    public void routeBuilderFromMethod() {
        RouteBuilder routeBuilder = CamelZeebeRule
            .routeBuilderFromMethod("getRoute", RouteBuilderMethod.class, new RouteBuilderMethod());

        assertThat(routeBuilder).isNotNull();
    }

    @Test
    public void routeBuilderFromField() {
        RouteBuilder routeBuilder = CamelZeebeRule
            .routeBuilderFromField("route", RouteBuilderField.class, new RouteBuilderField());

        assertThat(routeBuilder).isNotNull();
    }

    static class RouteBuilderMethod {

        private RouteBuilder getRoute() {
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:foo").to("direct:bar");
                }
            };
        }

    }

    static class RouteBuilderField {

        private RouteBuilder route = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:foo").to("direct:bar");
            }
        };

    }
}

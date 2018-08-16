package io.zeebe.camel.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Rule;
import org.junit.Test;

@Slf4j
public class DummyComponentWithRuleTest {

    @Rule
    public final CamelZeebeRule runner = new CamelZeebeRule(this, new DefaultCamelContext(),
        "io.zeebe.camel.test.DummyComponent");

    private final RouteBuilder routeBuilder = new RouteBuilder() {
        @Override
        public void configure() throws Exception {
            from("direct:foo").to("log:test");
        }
    };

    @Test
    @CamelZeebeTest(routeBuilder = "routeBuilder")
    public void name() {

        log.warn("calling name!");

    }

}

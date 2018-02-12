package io.zeebe.camel.test;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Rule;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CamelZeebeRuleTest {

    @Rule
    public final CamelZeebeRule runner = new CamelZeebeRule(this);


    @Test
    @CamelZeebeTest(routeBuilder = "routeBuilder")
    public void name() {

        log.warn("calling name!");


    }

    private RouteBuilder routeBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:foo").to("log:test");
            }
        };
    }
}

package io.zeebe.camel.examle.rnd;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Ignore
public class RndComponentTest extends CamelTestSupport {



    @Test
    public void testRnd() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("rnd:foo?generator=alphabetic&length=30")
                    .to("stream:out");
            }
        };
    }
}

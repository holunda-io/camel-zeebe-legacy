package io.zeebe.camel.examle.rnd;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RndApplication {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("rnd:foo?generator=alphabetic&length=30")
                    .log()
                    .to("stream:out");
            }
        });
        main.run(args);
    }
}

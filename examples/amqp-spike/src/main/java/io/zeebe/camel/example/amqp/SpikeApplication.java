package io.zeebe.camel.example.amqp;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpikeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpikeApplication.class, args);
    }

    @Bean
    public RouteBuilder fooQueue() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("activemq:foo").to("log:sample");

                from("timer:bar").setBody(constant("Hello from Camel")).to("activemq:foo");
            }

        };
    }
}

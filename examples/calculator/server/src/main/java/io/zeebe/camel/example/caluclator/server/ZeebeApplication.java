package io.zeebe.camel.example.caluclator.server;

import io.zeebe.camel.ZeebeComponent;
import io.zeebe.camel.message.ProcessDeployMessage;
import io.zeebe.camel.processor.DeployFromFileProcessor;
import io.zeebe.camel.processor.FromFileToProcessDeployMessage;
import io.zeebe.spring.broker.EnableZeebeBroker;
import io.zeebe.spring.client.EnableZeebeClient;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFileMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZeebeBroker
@EnableZeebeClient
public class ZeebeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeebeApplication.class);
    }

    @Value("${zeebe.sourceDir}")
    private String dir;

    @Bean
    public ZeebeComponent zeebeComponent(final ZeebeClientLifecycle lifecycle) {
        return new ZeebeComponent(lifecycle::get);
    }

    @Bean
    public RouteBuilder deployFromFile() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(
                    "file:" + dir + "?include=.*\\.bpmn")
                    .bean(FromFileToProcessDeployMessage.INSTANCE)
                    .to("zeebe:process/deploy")
                ;
            }
        };
    }
}

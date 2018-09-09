package io.zeebe.camel.example.calculator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.DataFormat;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }

    @Value("${zeebe.sourceDir}")
    private String sourceDir;

    @Value("${zeebe.destDir}")
    private String destDir;

    @Bean
    public RouteBuilder deploy() {
        return new RouteBuilder() {

            @Override
            public void configure() {
                from("file:" + sourceDir)
                    .to("file:" + destDir);
            }
        };
    }
}

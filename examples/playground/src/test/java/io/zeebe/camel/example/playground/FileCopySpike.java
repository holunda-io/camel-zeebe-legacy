package io.zeebe.camel.example.playground;

import java.io.File;
import java.util.stream.Stream;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileCopySpike {

    @Rule
    public final TemporaryFolder folder1 = new TemporaryFolder();
    @Rule
    public final TemporaryFolder folder2 = new TemporaryFolder();

    @Test
    public void copy() throws Exception {
        int i = 1;
        folder1.create();
        folder2.create();
        File file = folder1.newFile(i++ + ".txt");

        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("file:" + folder1.getRoot().getAbsolutePath() + "?noop=true")
                    .to("file:" + folder2.getRoot());
            }
        });

        context.start();

        for (int j = 0; j < 5; j++) {
            folder1.newFile(i++ + ".txt");
            Thread.sleep(1000);
        }

        Thread.sleep(10000);
        context.stop();

        Stream.of(folder2.getRoot().listFiles()).map(File::getAbsolutePath)
            .forEach(System.out::println);

    }
}

package io.zeebe.camel.examle.rnd;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.processor.aggregate.AggregationStrategy;

@Slf4j
public class RndApplication {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("rnd:foo?generator=alphanumeric&initialDelay=0&delay=50&length=10")
                    .setHeader("foo", constant("bar"))
                    .aggregate(header("foo"), new ListAggregationStrategy())
                    .completionSize(5)
                    .to("log:net.javaforge.blog.camel?level=INFO");
            }
        });
        main.run(args);
    }

    private static class ListAggregationStrategy implements AggregationStrategy
    {

        @SuppressWarnings("unchecked")
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            Object newBody = newExchange.getIn().getBody();
            List<Object> list;
            if (oldExchange == null) {
                list = new ArrayList<Object>();
                list.add(newBody);
                newExchange.getIn().setBody(list);
                return newExchange;
            } else {
                list = oldExchange.getIn().getBody(List.class);
                list.add(newBody);
                return oldExchange;
            }
        }
    }
}

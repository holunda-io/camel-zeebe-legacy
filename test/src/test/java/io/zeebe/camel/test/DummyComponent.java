package io.zeebe.camel.test;

import io.zeebe.client.ZeebeClient;
import java.util.Map;
import java.util.function.Supplier;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.DefaultProducer;

public class DummyComponent extends DefaultComponent {

    public static final String FQN = "io.zeebe.camel.test.DummyComponent";
    public static final String SCHEME = "dummy";

    private final Supplier<ZeebeClient> zeebeClient;

    public DummyComponent(final Supplier<ZeebeClient> zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters)
        throws Exception {
        return new DefaultEndpoint() {
            @Override
            public Producer createProducer() throws Exception {
                return new DefaultProducer(this) {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setBody(SCHEME);
                    }
                };
            }

            @Override
            public Consumer createConsumer(Processor processor) throws Exception {
                return new DefaultConsumer(this, processor);
            }

            @Override
            public boolean isSingleton() {
                return true;
            }
        };
    }

    public ZeebeClient getClient() {
        return zeebeClient.get();
    }
}

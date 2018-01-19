package io.zeebe.camel;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Zeebe endpoint.
 */
@UriEndpoint(
    firstVersion = "0.0.1-SNAPSHOT",
    scheme = "zeebe",
    title = "Zeebe",
    syntax="zeebe:name",
    consumerClass = ZeebeConsumer.class,
    label = "custom")
@NoArgsConstructor
@Data
public class ZeebeEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String name;

    /**
     * Some description of this option, and what it does
     */
    @UriParam(defaultValue = "10")
    private int option = 10;

    public ZeebeEndpoint(String uri, ZeebeComponent component) {
        super(uri, component);
    }

    public ZeebeEndpoint(String endpointUri) {
        super(endpointUri);
    }

    @Override
    public Producer createProducer() throws Exception {
        return new ZeebeProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new ZeebeConsumer(this, processor);
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setOption(int option) {
        this.option = option;
    }

    public int getOption() {
        return option;
    }
}

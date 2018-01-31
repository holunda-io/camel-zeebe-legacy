package io.zeebe.camel;

import java.util.Properties;
import java.util.function.Supplier;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriPath;

import io.zeebe.client.ZeebeClient;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Zeebe endpoint.
 */
@UriEndpoint(firstVersion = "0.0.1-SNAPSHOT", scheme = "zeebe", title = "Zeebe", syntax = "zeebe:name", consumerClass = ZeebeConsumer.class, label = "custom")
@NoArgsConstructor
@Data
public class ZeebeEndpoint extends DefaultEndpoint implements ClientSupplier
{

   private ZeebeComponent component;

    /**
     * The name.
     */
    @UriPath @Metadata(required = "true")
    private String name;

    /**
     * just to fill the space.
     * TODO: remove
     */
    @UriPath
    private String option;


    public ZeebeEndpoint(final String uri, final ZeebeComponent component)
    {
        super(uri, component);
        this.component = component;

    }

    @Override
    public Producer createProducer() throws Exception
    {
        return new ZeebeProducer(this);
    }

    @Override
    public Consumer createConsumer(final Processor processor) throws Exception
    {
        return new ZeebeConsumer(this, processor);
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }

    @Override
    public ZeebeClient getClient()
    {
        return component.getClient();
    }
}

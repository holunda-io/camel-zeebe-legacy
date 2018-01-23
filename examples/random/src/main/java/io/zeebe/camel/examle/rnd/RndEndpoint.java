package io.zeebe.camel.examle.rnd;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.ScheduledPollEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Rnd endpoint.
 */
@UriEndpoint(
    firstVersion = "0.0.1-SNAPSHOT",
    scheme = "rnd",
    title = "Rnd",
    syntax = "rnd:name",
    consumerClass = RndConsumer.class,
    label = "custom")
@Data
@NoArgsConstructor
public class RndEndpoint extends ScheduledPollEndpoint {

    // javadoc on attributes must not be removed, needed for maven-camel!
    /**
     * The name.
     */
    @UriPath @Metadata(required = "true")
    private String name;

    /**
     * The generator
     */
    @UriParam
    private RndGenerator generator = RndGenerator.RANDOM;

    /**
     * the length
     */
    @UriParam
    private int length = 10;

    /**
     * the chars.
     */
    @UriParam
    private String chars = null;

    /**
     * use letters.
     */
    @UriParam
    private boolean letters = false;

    /**
     * use numbers.
     */
    @UriParam
    private boolean numbers = false;

    /**
     * start with.
     */
    @UriParam
    private int start = 0;

    /**
     * end
     */
    @UriParam
    private int end = 0;

    public RndEndpoint(String uri, RndComponent component) {
        super(uri, component);
    }

    @Override
    public Producer createProducer() throws Exception {
        return new RndProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new RndConsumer(this, processor);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}

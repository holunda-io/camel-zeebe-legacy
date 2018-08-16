package io.zeebe.camel.examle.rnd;

import java.util.Map;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link RndEndpoint}.
 */
public class RndComponent extends DefaultComponent {

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters)
        throws Exception {
        Endpoint endpoint = new RndEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}

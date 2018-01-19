package io.zeebe.camel;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link ZeebeEndpoint}.
 */
public class ZeebeComponent extends DefaultComponent {
    
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new ZeebeEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}

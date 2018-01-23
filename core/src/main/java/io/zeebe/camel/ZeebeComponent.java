package io.zeebe.camel;

import java.util.Map;
import java.util.function.Supplier;

import org.apache.camel.Endpoint;
import io.zeebe.client.ZeebeClient;


import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link ZeebeEndpoint}.
 */
public class ZeebeComponent extends DefaultComponent
{


    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new ZeebeEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }

}

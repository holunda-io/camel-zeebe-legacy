package io.zeebe.camel;

import java.util.Map;
import java.util.Objects;

import io.zeebe.camel.endpoint.EndpointConfiguration;
import io.zeebe.camel.endpoint.UniversalEventHandlerEndpoint;
import io.zeebe.camel.fn.ClientSupplier;
import io.zeebe.client.ZeebeClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * The zeebe core component.
 */
@Slf4j
public class ZeebeComponent extends DefaultComponent implements ClientSupplier
{
    public static final String SCHEME = "zeebe";

    private final ZeebeClient client;

    public ZeebeComponent(final ZeebeClient client)
    {
        this.client = Objects.requireNonNull(client, "client must not be null!");
    }

    @Override
    protected Endpoint createEndpoint(final String uri, final String remaining, final Map<String, Object> parameters) throws Exception
    {
        final EndpointConfiguration configuration = EndpointConfiguration.builder()
            .uri(uri)
            .remaining(remaining)
            .parameters(parameters)
            .build();
        log.info("creating endpoint configuration={}", configuration);

        Endpoint endpoint = null;
        switch (remaining) {
        case "universalEventHandler":
            endpoint = new UniversalEventHandlerEndpoint(configuration, this);
            break;
        default:
            throw new IllegalStateException(String.format("unsupported syntax: '%s'", remaining ));
        }
        setProperties(endpoint, parameters);
        return endpoint;
    }

    @Override
    public ZeebeClient getClient()
    {
        return client;
    }
}

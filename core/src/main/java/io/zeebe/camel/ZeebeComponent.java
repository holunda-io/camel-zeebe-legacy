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
 * The zeebe core component. It is identified by camel via the {@link ZeebeComponent#SCHEME} keyword.
 * To get registered, this class is bound to the scheme in <code>/META-INF/services/org/apache/camel/component/zeebe</code>.
 */
@Slf4j
public class ZeebeComponent extends DefaultComponent implements ClientSupplier
{
    public static final String SCHEME = "zeebe";

    private final ZeebeClient client;

    /**
     * A new zeebe camel component is created with an existing client. The configuration/creation of the client is not
     * in the responsibility of this component.
     *
     * <pre>
     *     ZeebeClient client = ....;
     *     camelContext.addComponent(ZeebeComponent.SCHEME, new ZeebeComponent(client));
     * </pre>
     *
     * @param client the zeebe client
     */
    public ZeebeComponent(final ZeebeClient client)
    {
        this.client = Objects.requireNonNull(client, "client must not be null!");
    }

    /**
     *
     * @param uri the complete uri as used in the routeBuilder
     * @param remaining the part between <code>zeebe:</code> and the (optional) parameters. Used to identify the use case and what endpoint to create.
     * @param parameters
     * @return
     * @throws Exception
     */
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

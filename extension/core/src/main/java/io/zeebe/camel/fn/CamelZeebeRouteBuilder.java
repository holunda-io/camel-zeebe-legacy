package io.zeebe.camel.fn;

import java.util.Optional;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;

public class CamelZeebeRouteBuilder extends RouteBuilder {

    private final CamelZeebeUri camelZeebeUri;

    private Optional<String> logger = Optional.empty();
    private boolean json = false;
    private String target;

    public CamelZeebeRouteBuilder(final CamelZeebeUri camelZeebeUri) {
        this.camelZeebeUri = camelZeebeUri;
    }

    public CamelZeebeRouteBuilder logger(String name) {
        this.logger = Optional.of("log:" + name + "?showHeaders=true");
        return this;
    }

    public CamelZeebeRouteBuilder toJson() {
        this.json = true;
        return this;
    }

    public CamelZeebeRouteBuilder to(String target) {
        this.target = target;
        return this;
    }

    @Override
    public void configure() throws Exception {
        if (target == null) {
            throw new IllegalStateException("'to' must be defined.");
        }

        final RouteDefinition route = from(camelZeebeUri.get());

        if (json) {
            route.marshal().json(JsonLibrary.Jackson);
        }

        logger.ifPresent(route::to);

        route.to(target);
    }
}

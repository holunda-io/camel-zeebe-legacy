package io.zeebe.camel.fn;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

import io.zeebe.camel.EndpointConfiguration;
import io.zeebe.camel.ZeebeComponent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CamelZeebeUri<T extends CamelZeebeUri<T>> implements Supplier<String> {

    protected boolean uriFormat = true;
    protected final List<String> bases = new ArrayList<>();
    protected final Map<String, Object> parameters = new LinkedHashMap<>();

    public CamelZeebeUri(final String topic) {
        base(topic);
    }

    public String getBase() {
        return String.join(EndpointConfiguration.DELIMITER, bases);
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    protected T base(String base) {
        bases.add(requireNonNull(base));
        return (T) this;
    }

    protected T parameter(String key, Object value) {
        parameters.put(requireNonNull(key), requireNonNull(value));
        return (T) this;
    }

    public CamelZeebeRouteBuilder route() {
        return new CamelZeebeRouteBuilder(this);
    }

    @Override
    public String get() {
        final StringBuilder resultBuilder = new StringBuilder(ZeebeComponent.SCHEME);
        resultBuilder.append(uriFormat ? "://" : ":");
        resultBuilder.append(getBase());

        if (!parameters.isEmpty()) {
            resultBuilder.append("?" + parameters.entrySet().stream()
                .map(e -> String.format("%s=%s", e.getKey(), e.getValue())).collect(joining("&")));
        }
        return resultBuilder.toString();
    }
}

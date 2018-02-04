package io.zeebe.camel.fn;

import static java.util.stream.Collectors.joining;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.zeebe.camel.EndpointConfiguration;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

/**
 * Fluent Camel Endpoint uri builder
 *
 * @deprecated see {@link CamelZeebeUri}
 */
@Deprecated
@Builder(toBuilder = true)
public class CamelUri
{

    private String scheme;

    @Builder.Default
    private boolean uriFormat = true;

    @Builder.Default
    private String baseDelimiter = EndpointConfiguration.DELIMITER;

    @Singular
    private final List<String> bases;

    @Singular
    @Getter
    private final Map<String, Object> parameters;

    public String getBase() {
        return String.join(baseDelimiter, bases);
    }

    @Override
    public String toString()
    {
        final StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(scheme);
        resultBuilder.append(uriFormat ? "://" : ":");

        resultBuilder.append(getBase());

        if (!parameters.isEmpty())
        {
            resultBuilder.append(
                "?" + parameters.entrySet().stream().map(e -> String.format("%s=%s", e.getKey(), e.getValue())).collect(joining("&")));
        }
        return resultBuilder.toString();
    }
}

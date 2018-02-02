package io.zeebe.camel.fn;

import lombok.Builder;
import lombok.Singular;

import java.util.Iterator;
import java.util.Map;

/**
 * Fluent Camel Endpoint uri builder
 */
@Builder(toBuilder = true)
public class CamelUriBuilder {

    private String scheme;
    private String base;

    @Builder.Default
    private boolean uriFormat = true;

    @Singular
    private final Map<String, String> parameters;


    @Override
    public String toString() {
        final StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(scheme);
        resultBuilder.append(uriFormat ? "://" : ":");
        resultBuilder.append(base);
        if (!parameters.isEmpty()) {
            resultBuilder.append("?");
            final Iterator<Map.Entry<String, String>> paramIterator = parameters.entrySet().iterator();
            if (paramIterator.hasNext()) {
                final Map.Entry<String, String> first = paramIterator.next();
                resultBuilder.append(first.getKey()).append("=").append(first.getValue());
                paramIterator.forEachRemaining(entry -> resultBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue()));
            }
        }
        return resultBuilder.toString();
    }
}

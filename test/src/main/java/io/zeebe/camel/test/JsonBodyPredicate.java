package io.zeebe.camel.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

public abstract class JsonBodyPredicate implements Predicate {

    private final ObjectMapper mapper;

    public JsonBodyPredicate() {
        this(new ObjectMapper());
    }

    public JsonBodyPredicate(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public abstract void matches(final JsonNode json);

    @Override
    public boolean matches(final Exchange exchange) {
        try {
            matches(mapper.readTree(exchange.getIn().getBody(String.class)));
        } catch (final IOException e) {
            throw new IllegalArgumentException(e);
        }

        return true;
    }


}

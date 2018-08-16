package io.zeebe.camel.handler.universal;

import io.zeebe.camel.fn.CamelZeebeUri;
import java.util.UUID;

public class UniversalEventUri extends CamelZeebeUri<UniversalEventUri> {

    public static UniversalEventUri topic(final String topic) {
        return new UniversalEventUri(topic);
    }

    public UniversalEventUri(final String topic) {
        super(topic);
        // subscription name has a limit of 32 chars in zeebe
        name(UUID.randomUUID().toString().substring(0, 32));
    }

    public UniversalEventUri name(final String name) {
        if (name == null || "".equals(name.trim()) || name.length() > 32) {
            throw new IllegalArgumentException(
                "name must be non null/blank and has a max length of 32.");
        }
        parameter("name", name);
        return this;
    }

}

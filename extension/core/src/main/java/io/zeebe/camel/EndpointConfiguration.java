package io.zeebe.camel;

import io.zeebe.camel.api.command.SomeCommand;
import io.zeebe.camel.handler.Handler;
import io.zeebe.camel.handler.universal.UniversalEventEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.ToString;

@ToString
public class EndpointConfiguration {

    private static final String TOPIC = "topic";
    private static final String SUBJECT = "subject";
    private static final String TYPE = "type";
    public static final String DELIMITER = "/";

    public static Remaining remaining(String remaining) {
        return new Remaining(remaining);
    }


    @Getter
    @ToString
    public static class Remaining {
        SomeCommand cmd = new SomeCommand("foo");

        private final String topic;
        private final Optional<String> subject;
        private final Optional<String> type;

        private Remaining(final String remaining) {
            if (remaining == null || "".equals(remaining.trim())) {
                throw new IllegalArgumentException("Remaining must not be null or blank.");
            }

            final String[] parts = remaining.split(DELIMITER);
            this.topic = parts[0];
            this.subject = (parts.length > 1) ? Optional.of(parts[1]) : Optional.empty();
            this.type = (parts.length > 2) ? Optional.of(parts[2]) : Optional.empty();
        }

    }

    @Getter
    private final String uri;

    @Getter
    private final Map<String, Object> parameters = new HashMap<>();

    @Getter
    private final ZeebeComponent component;

    @Getter
    private final Handler handler;

    private final Remaining remaining;


    public EndpointConfiguration(final String uri, final String remaining,
        final Map<String, Object> parameters, final ZeebeComponent component) {
        this.uri = uri;
        this.remaining = remaining(remaining);

        handler = Objects.requireNonNull(Handler.BY_SUBJECT.get(getSubject()), String
            .format("Unsupported syntax: '%s', use one of %s", getSubject(),
                Handler.BY_SUBJECT.keySet()));

        this.parameters.putAll(parameters);
        this.parameters.put(TOPIC, this.remaining.topic);
        this.parameters.put(SUBJECT, this.remaining.subject.orElse(null));
        this.parameters.put(TYPE, this.remaining.type.orElse(null));

        this.component = component;
    }

    public String getTopic() {
        return remaining.topic;
    }

    public String getSubject() {
        return remaining.subject.orElse(UniversalEventEndpoint.SUBJECT);
    }

    public ZeebeEndpoint createEndpoint() {
        return getHandler().createEndpoint(this);
    }
}

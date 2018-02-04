package io.zeebe.camel;

import java.util.*;

import io.zeebe.camel.handler.Handler;
import io.zeebe.camel.handler.universal.UniversalEventEndpoint;
import lombok.Getter;
import lombok.ToString;

@ToString
public class EndpointConfiguration
{
    public static Remaining remaining(String remaining) {
        return new Remaining(remaining);
    }
    public static final String DELIMITER = "/";

    @Getter
    @ToString
    public static class Remaining {

        private final String topic;
        private final Optional<String> subject;
        private final Optional<String> type;

        private Remaining(final String remaining)
        {
            if (remaining == null || "".equals(remaining.trim())) {
                throw new IllegalArgumentException("remaining must not be null or blank!");
            }

            final String[] parts = remaining.split(DELIMITER);
            this.topic = parts[0];
            this.subject = (parts.length > 1) ? Optional.of(parts[1]) : Optional.empty();
            this.type = (parts.length > 2) ? Optional.of(parts[2]) : Optional.empty();
        }

    }

    /**
     *
     */
    @Getter
    private final String uri;

    @Getter
    private final Map<String,Object> parameters = new HashMap<>();

    @Getter
    private final ZeebeComponent component;

    @Getter
    private final Handler handler;

    private final Remaining remaining;


    public EndpointConfiguration(final String uri, final String remaining, final Map<String, Object> parameters, final ZeebeComponent component)
    {
        this.uri = uri;
        this.remaining = remaining(remaining);

        handler = Objects.requireNonNull(Handler.BY_SUBJECT.get(getSubject()), String.format("Unsupported syntax: '%s', use one of %s", getSubject(), Handler.BY_SUBJECT.keySet()));

        this.parameters.putAll(parameters);
        this.parameters.put("topic", this.remaining.topic);
        this.parameters.put("subject", this.remaining.subject.orElse(null));
        this.parameters.put("type", this.remaining.type.orElse(null));

        this.component = component;
    }

    public String getTopic() {
        return remaining.topic;
    }

    public String getSubject() {
        return remaining.subject.orElse(UniversalEventEndpoint.SUBJECT);
    }

    public ZeebeEndpoint createEndpoint()
    {
        return getHandler().createEndpoint(this);
    }
}

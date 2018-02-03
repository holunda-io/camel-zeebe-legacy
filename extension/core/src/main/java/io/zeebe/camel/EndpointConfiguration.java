package io.zeebe.camel;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import io.zeebe.camel.ZeebeComponent;
import io.zeebe.camel.handler.Handler;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EndpointConfiguration
{
    private final String uri;
    private final String remaining;
    private final Map<String,Object> parameters;
    private final ZeebeComponent component;

    public static final String REMAINING_SPLIT = ":";

    public Handler getHandler() {
        return Objects.requireNonNull(Handler.BY_SUBJECT.get(getSubject()), String.format("Unsupported syntax: '%s', use one of %s", getSubject(), Handler.BY_SUBJECT.keySet()));
    }

    public String getSubject() {
        return remaining.split(REMAINING_SPLIT)[0];
    }

    public String getOperation() {
        return remaining.split(REMAINING_SPLIT)[1];
    }

    public ZeebeEndpoint createEndpoint()
    {
        return getHandler().createEndpoint(this);
    }
}

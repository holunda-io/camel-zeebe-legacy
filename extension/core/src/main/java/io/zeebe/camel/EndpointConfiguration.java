package io.zeebe.camel;

import java.io.Serializable;
import java.util.Map;

import io.zeebe.camel.ZeebeComponent;
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

    public String getSubject() {
        return remaining.split(REMAINING_SPLIT)[0];
    }

    public String getOperation() {
        return remaining.split(REMAINING_SPLIT)[1];
    }
}

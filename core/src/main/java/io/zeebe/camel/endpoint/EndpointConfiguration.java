package io.zeebe.camel.endpoint;

import java.io.Serializable;
import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EndpointConfiguration implements Serializable
{
    private final String uri;
    private final String remaining;
    private final Map<String,Object> parameters;
}

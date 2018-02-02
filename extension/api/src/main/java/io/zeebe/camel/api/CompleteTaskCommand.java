package io.zeebe.camel.api;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompleteTaskCommand
{
    private TaskEvent task;

    private String payload;
}

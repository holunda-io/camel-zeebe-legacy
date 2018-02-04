package io.zeebe.camel.api.command;

import io.zeebe.camel.api.event.TaskEvent;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompleteTaskCommand implements TaskCommand
{
    private TaskEvent task;

    private String payload;

    public boolean hasPayload() {
        return payload != null;
    }
}

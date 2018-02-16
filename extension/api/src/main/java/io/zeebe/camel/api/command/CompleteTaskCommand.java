package io.zeebe.camel.api.command;

import io.zeebe.camel.api.event.TaskEvent;
import lombok.Builder;
import lombok.Value;

/**
 * Command for completing the task.
 */
@Value
@Builder
public class CompleteTaskCommand implements TaskCommand
{
    /**
     * Task event as a basis for this command.
     */
    private final TaskEvent task;

    /**
     * Command payload. May be null.
     */
    private final String payload;

    /**
     * Determines if the payload exists.
     * @return <code>true</code> if payload is set.
     */
    public boolean hasPayload() {
        return payload != null;
    }

    @Override
    public CommandType getCommandType()
    {
        return CommandType.COMPLETE;
    }
}

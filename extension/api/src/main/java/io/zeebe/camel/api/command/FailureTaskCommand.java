package io.zeebe.camel.api.command;

import io.zeebe.camel.api.event.TaskEvent;
import lombok.Builder;
import lombok.Value;

/**
 * Command indicating failure by processing of the task.
 */
@Value
@Builder
public class FailureTaskCommand implements TaskCommand
{
    /**
     * Task event as a basis for this command.
     */
    private TaskEvent task;
}

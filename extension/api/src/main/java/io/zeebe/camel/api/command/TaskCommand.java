package io.zeebe.camel.api.command;

import io.zeebe.camel.api.event.TaskEvent;

/**
 * Task command.
 */
public interface TaskCommand
{
    /**
     * Retrieves the event for this task command.
     * @return task event.
     */
    TaskEvent getTask();
}

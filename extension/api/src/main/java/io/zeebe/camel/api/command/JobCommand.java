package io.zeebe.camel.api.command;

import io.zeebe.camel.api.event.JobEvent;

/**
 * Task command.
 */
public interface JobCommand {

    /**
     * Retrieves the event for this job command.
     *
     * @return job event.
     */
    JobEvent getJobEvent();


    CommandType getCommandType();
}

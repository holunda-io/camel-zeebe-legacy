package io.zeebe.camel.api.command;

import io.zeebe.camel.api.event.JobEvent;
import lombok.Builder;
import lombok.Value;

/**
 * Command indicating failure by processing of the job.
 */
@Value
@Builder
public class FailureJobCommand implements JobCommand {

    /**
     * Task event as a basis for this command.
     */
    private JobEvent jobEvent;


    @Override
    public CommandType getCommandType() {
        return CommandType.FAIL;
    }
}

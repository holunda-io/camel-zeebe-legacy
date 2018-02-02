package io.zeebe.camel.handler;

import static io.zeebe.camel.handler.Handler.TASK;
import static io.zeebe.camel.handler.Handler.UNIVERSAL_EVENT;
import static org.assertj.core.api.Assertions.assertThat;

import io.zeebe.camel.handler.task.TaskEndpoint;
import io.zeebe.camel.handler.universal.UniversalEventEndpoint;
import org.junit.Test;

public class HandlerTest
{

    @Test
    public void valueMap()
    {
        assertThat(Handler.VALUES).hasSize(2);
        assertThat(Handler.VALUES.get(TaskEndpoint.SUBJECT)).isEqualTo(TASK);
        assertThat(Handler.VALUES.get(UniversalEventEndpoint.SUBJECT)).isEqualTo(UNIVERSAL_EVENT);
    }

    @Test
    public void taskHandler()
    {
        assertThat(TASK.getSubject()).isEqualTo(TaskEndpoint.SUBJECT);
    }

    @Test
    public void universalEventHandler()
    {
        assertThat(UNIVERSAL_EVENT.getSubject()).isEqualTo(UniversalEventEndpoint.SUBJECT);
    }
}

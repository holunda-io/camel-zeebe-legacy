package io.zeebe.camel.handler;

import static io.zeebe.camel.handler.Handler.TASK;
import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;

import io.zeebe.camel.handler.task.TaskEndpoint;
import org.junit.Test;

public class HandlerTest {

    @Test
    public void valueMap() {
        assertThat(Handler.BY_SUBJECT).hasSize(2);
        assertThat(Handler.BY_SUBJECT.get(TaskEndpoint.SUBJECT)).isEqualTo(TASK);


        // FIXME assertThat(Handler.BY_SUBJECT.get(UniversalEventEndpoint.SUBJECT))
        // FIXME .isEqualTo(UNIVERSAL_EVENT);
    }

    @Test
    public void taskHandler() {
        assertThat(TASK.getSubject()).isEqualTo(TaskEndpoint.SUBJECT);
    }

    @Test
    public void universalEventHandler() {
        // FIXME assertThat(UNIVERSAL_EVENT.getSubject()).isEqualTo(UniversalEventEndpoint.SUBJECT);
        fail();
    }
}

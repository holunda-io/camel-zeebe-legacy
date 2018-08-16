package io.zeebe.camel.handler.task;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TaskUriTest {

    @Test
    public void create() {
        assertThat(TaskUri.topic("topic").type("type").lockOwner("owner").get())
            .isEqualTo("zeebe://topic/job/type?owner=owner");
    }
}

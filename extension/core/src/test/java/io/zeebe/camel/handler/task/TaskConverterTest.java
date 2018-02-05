package io.zeebe.camel.handler.task;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.zeebe.camel.api.event.EventMetadata;
import io.zeebe.camel.api.event.TaskEvent;
import io.zeebe.client.event.impl.TaskEventImpl;

public class TaskConverterTest {

    private static final String PAYLOAD = "{\"foo\":\"bar\"}";


    @Test
    public void convert_back_and_forth() {
        TaskEvent event = TaskEvent.builder()
            .metadata(EventMetadata.builder()
                .key(1L)
                .partitionId(1)
                .position(2L)
                .topicName("topic")
                .build())
            .payload(PAYLOAD)
            .lockOwner("owner")
            .customHeader("hello", "world")
            .header("foo", "bar")
            .lockTime(1L)
            .retries(3)
            .state("CREATED")
            .type("taskType")
            .build();

        TaskEventImpl impl = TaskConverter.convert(event);

        assertThat(TaskConverter.convert(impl)).isEqualTo(event);

    }
}

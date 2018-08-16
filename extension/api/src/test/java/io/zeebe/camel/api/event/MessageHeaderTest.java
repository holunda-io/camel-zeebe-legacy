package io.zeebe.camel.api.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MessageHeaderTest {

    @Test
    public void to_and_from_map() {
        MessageHeader header = MessageHeader.builder()
            .key(1L)
            .position(2L)
            .partitionId(1)
            .state("CREATE")
            .topicName("topic")
            .type("TASK")
            .build();

        assertThat(MessageHeader.from(header.toMap())).isEqualTo(header);
    }
}

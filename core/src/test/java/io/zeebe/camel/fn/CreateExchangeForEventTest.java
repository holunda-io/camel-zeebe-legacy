package io.zeebe.camel.fn;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import io.zeebe.client.event.EventMetadata;
import io.zeebe.client.event.TopicEventType;
import io.zeebe.client.event.impl.EventMetadataImpl;
import org.junit.Test;

public class CreateExchangeForEventTest
{

    @Test
    public void create_header_map()
    {
        final EventMetadataImpl metadata = new EventMetadataImpl();
        metadata.setEventKey(1111L);
        metadata.setEventPosition(2222L);
        metadata.setEventType(TopicEventType.UNKNOWN);
        metadata.setPartitionId(1);
        metadata.setTopicName("topic");

        final Map<String, Object> header = CreateExchangeForEvent.createHeader.apply(metadata);

        assertThat(header).isNotEmpty()
                          .containsEntry("topicName", "topic")
                          .containsEntry("partitionId", 1)
                          .containsEntry("position", 2222L)
                          .containsEntry("eventType", TopicEventType.UNKNOWN)
                          .containsEntry("key", 1111L)
        ;
    }
}

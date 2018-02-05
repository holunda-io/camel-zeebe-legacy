package io.zeebe.camel.handler.universal;

import org.apache.camel.Converter;

import io.zeebe.camel.api.event.EventMetadata;
import io.zeebe.camel.api.event.GeneralEvent;
import io.zeebe.client.event.impl.GeneralEventImpl;

@Converter
public class GeneralEventConverter
{
    @Converter
    public static GeneralEvent  convert(final GeneralEventImpl impl) {
        return GeneralEvent.builder()
            .json(impl.getJson())
            .state(impl.getState())
            .metadata(EventMetadata.builder()
                .topicName(impl.getMetadata().getTopicName())
                .key(impl.getMetadata().getKey())
                .partitionId(impl.getMetadata().getPartitionId())
                .position(impl.getMetadata().getPosition())
                .build())
            .build();
    }


    @Converter
    public static GeneralEventImpl  convert(final GeneralEvent event) {
        return null;
    }
}

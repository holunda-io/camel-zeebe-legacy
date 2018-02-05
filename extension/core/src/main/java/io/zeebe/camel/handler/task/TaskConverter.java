package io.zeebe.camel.handler.task;

import java.util.Optional;

import org.apache.camel.Converter;

import io.zeebe.camel.api.event.EventMetadata;
import io.zeebe.camel.api.event.TaskEvent;
import io.zeebe.client.event.impl.TaskEventImpl;
import io.zeebe.client.impl.data.MsgPackConverter;

@Converter
public class TaskConverter
{

    @Converter
    public static TaskEvent convert(TaskEventImpl taskEvent)
    {
        final EventMetadata metadata = EventMetadata.builder()
                                                    .key(taskEvent.getMetadata().getKey())
                                                    .partitionId(taskEvent.getMetadata().getPartitionId())
                                                    .position(taskEvent.getMetadata().getPosition())
                                                    .topicName(taskEvent.getMetadata().getTopicName())
                                                    .build();

        return TaskEvent.builder()
                        .metadata(metadata)
                        .state(taskEvent.getState())
                        .customHeaders(taskEvent.getCustomHeaders())
                        .payload(taskEvent.getPayload())
                        .headers(taskEvent.getHeaders())
                        .lockTime(taskEvent.getLockTime())
                        .lockOwner(taskEvent.getLockOwner())
                        .retries(taskEvent.getRetries())
                        .type(taskEvent.getType())
                        .build();
    }

    @Converter
    public static TaskEventImpl convert(final TaskEvent taskEvent)
    {
        final TaskEventImpl zeebeTask = new TaskEventImpl(taskEvent.getState(), new MsgPackConverter());

        Optional.ofNullable(taskEvent.getCustomHeaders()).ifPresent(h -> zeebeTask.setCustomHeaders(h));
        Optional.ofNullable(taskEvent.getHeaders()).ifPresent(h -> zeebeTask.setHeaders(h));

        zeebeTask.setLockOwner(taskEvent.getLockOwner());
        zeebeTask.setLockTime(taskEvent.getLockTime());
        zeebeTask.setPayload(taskEvent.getPayload());
        zeebeTask.setRetries(taskEvent.getRetries());
        zeebeTask.setType(taskEvent.getType());

        // metadata
        zeebeTask.setEventPosition(taskEvent.getMetadata().getPosition());
        zeebeTask.setKey(taskEvent.getMetadata().getKey());
        zeebeTask.setTopicName(taskEvent.getMetadata().getTopicName());
        zeebeTask.setPartitionId(taskEvent.getMetadata().getPartitionId());

        return zeebeTask;

    }
}

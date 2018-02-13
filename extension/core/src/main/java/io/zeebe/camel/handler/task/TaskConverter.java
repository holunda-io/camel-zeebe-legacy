package io.zeebe.camel.handler.task;

import io.zeebe.camel.api.event.EventHeader;
import io.zeebe.camel.api.event.TaskEvent;
import io.zeebe.client.event.impl.TaskEventImpl;
import io.zeebe.client.impl.data.MsgPackConverter;
import org.apache.camel.Converter;

@Converter
public class TaskConverter
{

    @Converter
    public static TaskEvent convert(io.zeebe.client.event.TaskEvent taskEvent)
    {

        final EventHeader metadata = EventHeader.builder()
                                                .key(taskEvent.getMetadata().getKey())
                                                .partitionId(taskEvent.getMetadata().getPartitionId())
                                                .position(taskEvent.getMetadata().getPosition())
                                                .topicName(taskEvent.getMetadata().getTopicName())
                                                .build();

        return TaskEvent.builder()
                        .metadata(metadata)
                        .state(taskEvent.getState())
                        .customHeaders(taskEvent.getCustomHeaders())
                        .getPayload(taskEvent.getPayload())
                        .headers(taskEvent.getHeaders())
                        .lockTime(taskEvent.getLockExpirationTime().getEpochSecond())
                        .lockOwner(taskEvent.getLockOwner())
                        .retries(taskEvent.getRetries())
                        .type(taskEvent.getType())

                        .build();
    }

    @Converter
    public static TaskEventImpl convert(final TaskEvent taskEvent)
    {
        final TaskEventImpl zeebeTask = new TaskEventImpl(taskEvent.getState(), new MsgPackConverter());

        zeebeTask.setCustomHeaders(taskEvent.getCustomHeaders());
        zeebeTask.setHeaders(taskEvent.getHeaders());
        zeebeTask.setLockOwner(taskEvent.getLockOwner());
        zeebeTask.setLockTime(taskEvent.getLockTime());
        zeebeTask.setPayload(taskEvent.getGetPayload());
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

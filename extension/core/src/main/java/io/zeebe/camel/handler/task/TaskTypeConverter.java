package io.zeebe.camel.handler.task;

import static io.zeebe.client.event.TopicEventType.TASK;

import java.time.Instant;
import java.util.Map;

import io.zeebe.camel.api.EventMetadata;
import io.zeebe.camel.api.TaskEvent;
import io.zeebe.client.event.impl.TaskEventImpl;
import io.zeebe.client.impl.data.MsgPackConverter;
import org.apache.camel.Converter;

@Converter
public class TaskTypeConverter
{

    @Converter
    public static TaskEvent convert(io.zeebe.client.event.TaskEvent taskEvent)
    {

        EventMetadata metadata = EventMetadata.builder()
                                              .key(taskEvent.getMetadata().getKey())
                                              .partitionId(taskEvent.getMetadata().getPartitionId())
                                              .position(taskEvent.getMetadata().getPosition())
                                              .topicName(taskEvent.getMetadata().getTopicName())
                                              .build();

        final TaskEvent task = TaskEvent.builder()
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
        return task;
    }

    @Converter
    public static io.zeebe.client.event.impl.TaskEventImpl convert(final TaskEvent taskEvent)
    {
        TaskEventImpl zeebeTask = new TaskEventImpl(taskEvent.getState(), new MsgPackConverter());

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

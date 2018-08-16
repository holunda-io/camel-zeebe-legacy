package io.zeebe.camel.handler.task;

import org.apache.camel.Converter;

@Converter
public class TaskConverter {

    // FIXME: implement
//    @Converter
//    public static JobEvent convert(io.zeebe.client.event.TaskEvent taskEvent) {
//
//        final MessageHeader metadata = MessageHeader.builder()
//            .key(taskEvent.getMetadata().getKey())
//            .partitionId(taskEvent.getMetadata().getPartitionId())
//            .position(taskEvent.getMetadata().getPosition())
//            .topicName(taskEvent.getMetadata().getTopicName())
//            .build();
//
//        return JobEvent.builder()
//            .state(taskEvent.getState())
//            .customHeaders(taskEvent.getCustomHeaders())
//            .payload(taskEvent.getPayload())
//            .headers(taskEvent.getHeaders())
//            .lockTime(taskEvent.getLockExpirationTime().getEpochSecond())
//            .lockOwner(taskEvent.getLockOwner())
//            .retries(taskEvent.getRetries())
//            .type(taskEvent.getType())
//
//            .build();
//    }
//
//    @Converter
//    public static TaskEventImpl convert(final JobEvent jobEvent) {
//        final JobEventImpl zeebeTask = new JobEventImpl(jobEvent.getState(),
//            new MsgPackConverter());
//
//        zeebeTask.setCustomHeaders(jobEvent.getCustomHeaders());
//        zeebeTask.setHeaders(jobEvent.getHeaders());
//        zeebeTask.setLockOwner(jobEvent.getLockOwner());
//        zeebeTask.setLockTime(jobEvent.getLockTime());
//        zeebeTask.setPayload(jobEvent.getPayload());
//        zeebeTask.setRetries(jobEvent.getRetries());
//        zeebeTask.setType(jobEvent.getType());
//
//        // metadata
//        zeebeTask.setEventPosition(jobEvent.getMetadata().getPosition());
//        zeebeTask.setKey(jobEvent.getMetadata().getKey());
//        zeebeTask.setTopicName(jobEvent.getMetadata().getTopicName());
//        zeebeTask.setPartitionId(jobEvent.getMetadata().getPartitionId());
//
//        return zeebeTask;
//
//    }
}

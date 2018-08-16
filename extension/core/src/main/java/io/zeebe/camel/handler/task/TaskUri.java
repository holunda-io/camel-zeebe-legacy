package io.zeebe.camel.handler.task;

import io.zeebe.camel.fn.CamelZeebeUri;

public class TaskUri extends CamelZeebeUri<TaskUri> {

    public static TaskUri topic(final String topic) {
        return new TaskUri(topic);
    }

    public TaskUri(final String topic) {
        super(topic);
        base(TaskEndpoint.SUBJECT);
    }

    public TaskUri type(String taskType) {
        base(taskType);
        return this;
    }

    public TaskUri lockOwner(String lockOwner) {
        parameter("owner", lockOwner);
        return this;
    }
}

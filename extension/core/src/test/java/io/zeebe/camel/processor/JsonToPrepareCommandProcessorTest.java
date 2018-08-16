package io.zeebe.camel.processor;

import io.zeebe.camel.api.command.CommandType;
import io.zeebe.camel.processor.JsonToPrepareCommandProcessor.PrepareCommand;
import io.zeebe.client.api.record.ZeebeObjectMapper;
import io.zeebe.client.impl.data.ZeebeObjectMapperImpl;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.camel.test.junit4.ExchangeTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class JsonToPrepareCommandProcessorTest extends ExchangeTestSupport {

    // this is example data taken from the exchange log
    private static final Map<String, Object> header = Stream.of(
        "breadcrumbId=ID-farlane-1518730792196-0-10, key=4294977032, partitionId=1, position=4294978104, state=LOCKED, topicName=default-topic, type=TASK"
            .split(
                ",")).map(String::trim).map(s -> s.split("="))
        .collect(Collectors.toMap(p -> p[0], p -> p[1]));
    private static final String completeCommand = "{\"job\":{\"state\":\"LOCKED\",\"type\":\"doSomething\",\"headers\":{\"activityId\":\"task_doSomething\",\"workflowKey\":4294975304,\"workflowInstanceKey\":4294975520,\"bpmnProcessId\":\"process_dummy\",\"activityInstanceKey\":4294976512,\"workflowDefinitionVersion\":1},\"customHeaders\":{},\"lockOwner\":\"test\",\"retries\":3,\"lockTime\":1518768987210,\"payloadSource\":\"{\\\"bar\\\":\\\"hello\\\"}\",\"payload\":\"gaNiYXKlaGVsbG8=\"},\"payload\":\"\\\"bar\\\":\\\"world\\\"\",\"commandType\":\"COMPLETE\"}";

    private final JsonToPrepareCommandProcessor processor = new JsonToPrepareCommandProcessor(
        new ZeebeObjectMapperImpl());

    @Test
    public void prepare_command_with_task() throws Exception {
        exchange.getIn().setHeaders(header);
        exchange.getIn().setBody(completeCommand);

        processor.process(exchange);

        final PrepareCommand command = exchange.getIn().getBody(PrepareCommand.class);

        Assertions.assertThat(command).isNotNull();
        Assertions.assertThat(command.getCommandType()).isEqualTo(CommandType.COMPLETE);
        Assertions.assertThat(command.getJob().getPayload()).isEqualTo("{\"bar\":\"hello\"}");
        Assertions.assertThat(command.getJob().getCustomHeaders()).isEmpty();
        Assertions.assertThat(command.getJob().getHeaders())
            .hasSize(6)
            .containsEntry("activityId", "task_doSomething")
            .containsEntry("workflowKey", 4294975304L)
            .containsEntry("workflowInstanceKey", 4294975520L)
            .containsEntry("bpmnProcessId", "process_dummy")
            .containsEntry("activityInstanceKey", 4294976512L)
            .containsEntry("workflowDefinitionVersion", 1);
        // FIXME Assertions.assertThat(command.getJob().getLockOwner()).isEqualTo("test");
// FIXME          Assertions.assertThat(command.getJob().getLockTime()).isEqualTo(1518768987210L);
        Assertions.assertThat(command.getJob().getRetries()).isEqualTo(3);
        Assertions.assertThat(command.getJob().getType()).isEqualTo("doSomething");

        // FIXME final EventMetadata metadata = command.getJob().getMetadata();
        // FIXME Assertions.assertThat(metadata.getKey()).isEqualTo(4294977032L);
        // FIXME Assertions.assertThat(metadata.getPosition()).isEqualTo(4294978104L);
        // FIXME Assertions.assertThat(metadata.getPartitionId()).isEqualTo(1);
        // FIXME Assertions.assertThat(metadata.getTopicName()).isEqualTo("default-topic");
        // FIXME Assertions.assertThat(metadata.getType()).isEqualTo(TopicEventType.TASK);
    }
}

package io.zeebe.camel.processor;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.zeebe.camel.api.command.CommandType;
import io.zeebe.camel.processor.JsonToPrepareCommandProcessor.PrepareCommand;

import io.zeebe.client.event.EventMetadata;
import io.zeebe.client.event.TopicEventType;
import io.zeebe.client.impl.ZeebeObjectMapper;
import org.apache.camel.test.junit4.ExchangeTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class JsonToPrepareCommandProcessorTest extends ExchangeTestSupport
{

    // this is example data taken from the exchange log
    private static final Map<String, Object> header = Stream.of(
        "breadcrumbId=ID-farlane-1518730792196-0-10, key=4294977032, partitionId=1, position=4294978104, state=LOCKED, topicName=default-topic, type=TASK".split(
            ",")).map(String::trim).map(s -> s.split("=")).collect(Collectors.toMap(p -> p[0], p -> p[1]));
    private static final String completeCommand = "{\"task\":{\"state\":\"LOCKED\",\"type\":\"doSomething\",\"headers\":{\"activityId\":\"task_doSomething\",\"workflowKey\":4294975304,\"workflowInstanceKey\":4294975520,\"bpmnProcessId\":\"process_dummy\",\"activityInstanceKey\":4294976512,\"workflowDefinitionVersion\":1},\"customHeaders\":{},\"lockOwner\":\"test\",\"retries\":3,\"lockTime\":1518768987210,\"payloadSource\":\"{\\\"bar\\\":\\\"hello\\\"}\",\"payload\":\"gaNiYXKlaGVsbG8=\"},\"payload\":\"\\\"bar\\\":\\\"world\\\"\",\"commandType\":\"COMPLETE\"}";

    private final JsonToPrepareCommandProcessor processor = new JsonToPrepareCommandProcessor(new ZeebeObjectMapper());

    @Test
    public void prepare_command_with_task() throws Exception
    {
        exchange.getIn().setHeaders(header);
        exchange.getIn().setBody(completeCommand);

        processor.process(exchange);

        final PrepareCommand command = exchange.getIn().getBody(PrepareCommand.class);

        Assertions.assertThat(command).isNotNull();
        Assertions.assertThat(command.getCommandType()).isEqualTo(CommandType.COMPLETE);
        Assertions.assertThat(command.getTask().getPayload()).isEqualTo("{\"bar\":\"hello\"}");
        Assertions.assertThat(command.getTask().getCustomHeaders()).isEmpty();
        Assertions.assertThat(command.getTask().getHeaders())
                  .hasSize(6)
                  .containsEntry("activityId", "task_doSomething")
                  .containsEntry("workflowKey", 4294975304L)
                  .containsEntry("workflowInstanceKey", 4294975520L)
                  .containsEntry("bpmnProcessId", "process_dummy")
                  .containsEntry("activityInstanceKey", 4294976512L)
                  .containsEntry("workflowDefinitionVersion", 1);
        Assertions.assertThat(command.getTask().getLockOwner()).isEqualTo("test");
        Assertions.assertThat(command.getTask().getLockTime()).isEqualTo(1518768987210L);
        Assertions.assertThat(command.getTask().getRetries()).isEqualTo(3);
        Assertions.assertThat(command.getTask().getType()).isEqualTo("doSomething");

        final EventMetadata metadata = command.getTask().getMetadata();
        Assertions.assertThat(metadata.getKey()).isEqualTo(4294977032L);
        Assertions.assertThat(metadata.getPosition()).isEqualTo(4294978104L);
        Assertions.assertThat(metadata.getPartitionId()).isEqualTo(1);
        Assertions.assertThat(metadata.getTopicName()).isEqualTo("default-topic");
        Assertions.assertThat(metadata.getType()).isEqualTo(TopicEventType.TASK);
    }
}

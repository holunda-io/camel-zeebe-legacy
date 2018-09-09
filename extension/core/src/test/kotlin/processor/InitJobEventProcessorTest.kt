package io.zeebe.camel.processor

import io.zeebe.camel.jobEvent
import io.zeebe.client.api.events.JobEvent
import io.zeebe.client.impl.data.ZeebeObjectMapperImpl
import org.apache.camel.Exchange
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.impl.DefaultExchange
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class InitJobEventProcessorTest {

  val objectMapper = ZeebeObjectMapperImpl()

  val jobEvent = objectMapper.jobEvent("""
      {"metadata":{"intent":"ACTIVATED","valueType":"JOB","recordType":"EVENT","rejectionType":null,"topicName":"default-topic","partitionId":1,"key":4294970256,"position":4294971040,"sourceRecordPosition":4294971040,"timestamp":"2018-09-06T19:57:12.443Z","rejectionReason":null},"headers":{"activityId":"task_doSomething","workflowKey":1,"workflowInstanceKey":4294968672,"bpmnProcessId":"process_dummy","activityInstanceKey":4294969712,"workflowDefinitionVersion":1},"customHeaders":{},"deadline":"2018-09-06T20:02:12.443Z","worker":"default","retries":3,"type":"doSomething","payload":{"bar":"fe5b0cb7-3083-4d5e-80ee-69f8f8981679"}}
    """.trimIndent())

  val processor = InitJobEventProcessor(objectMapper)

  lateinit var exchange: Exchange

  @Before
  fun prepareExchange() {
    val camel = DefaultCamelContext()
    exchange = DefaultExchange(camel)
    val message = exchange.`in`
    message.body = jobEvent

    assertThat(message.headers).isEmpty()
    assertThat(message.getBody(JobEvent::class.java)).isEqualTo(jobEvent)
  }

  @Test
  fun `receive jobEvent and forward header-body`() {
    processor.process(exchange)
    val message = exchange.out

    assertThat(message.getHeader(InitJobEventProcessor.HEADER_JOB_EVENT)).isEqualTo(objectMapper.jobEvent(jobEvent))
    assertThat(message.body).isEqualTo(jobEvent.payload)
  }
}

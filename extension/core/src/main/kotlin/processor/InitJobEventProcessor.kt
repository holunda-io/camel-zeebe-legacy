package io.zeebe.camel.processor

import io.zeebe.camel.jobEvent
import io.zeebe.camel.processor.MessageProcessor.OutMessage
import io.zeebe.client.api.events.JobEvent
import io.zeebe.client.api.record.ZeebeObjectMapper
import org.apache.camel.Exchange
import org.apache.camel.Message
import org.apache.camel.Processor

/**
 * Stores jobEvent json in header and payload in body.
 */
class InitJobEventProcessor(
    private val objectMapper: ZeebeObjectMapper
) : MessageProcessor {


  companion object {
    const val HEADER_JOB_EVENT = "jobEvent"
  }

  override fun process(msg: Message): OutMessage {
    val jobEvent = msg.getBody(JobEvent::class.java)

    return OutMessage(
        headers = mapOf(HEADER_JOB_EVENT to objectMapper.jobEvent(jobEvent)),
        body = jobEvent.payload
    )
  }

}

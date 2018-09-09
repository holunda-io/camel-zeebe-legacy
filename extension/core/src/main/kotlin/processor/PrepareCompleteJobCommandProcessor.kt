package io.zeebe.camel.processor

import com.fasterxml.jackson.databind.ObjectMapper
import io.zeebe.camel.api.Json
import io.zeebe.camel.api.command.CompleteJobCommand
import io.zeebe.camel.jobEvent
import io.zeebe.camel.processor.MessageProcessor.OutMessage
import io.zeebe.client.api.record.ZeebeObjectMapper
import org.apache.camel.Exchange
import org.apache.camel.Message
import org.apache.camel.Processor

/**
 * Reads jobEvent from header
 */
class PrepareCompleteJobCommandProcessor(private val zeebeObjectMapper: ZeebeObjectMapper) : MessageProcessor {

  private val objectMapper = ObjectMapper()


  override fun process(msg: Message): OutMessage {
    val jobEventJson = msg.getHeader(InitJobEventProcessor.HEADER_JOB_EVENT) as String
    val payload = msg.getBody(Json::class.java)

    return OutMessage(body = CompleteJobCommand(jobEventJson, payload))
  }
}

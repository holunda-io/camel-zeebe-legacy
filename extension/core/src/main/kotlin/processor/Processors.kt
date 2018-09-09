package io.zeebe.camel.processor

import io.zeebe.camel.message.ProcessDeployMessage
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.component.file.GenericFileMessage

object FromFileToProcessDeployMessage : Processor {
  override fun process(exchange: Exchange) = with(exchange) {
    out = ProcessDeployMessage(getIn(GenericFileMessage::class.java))
  }
}

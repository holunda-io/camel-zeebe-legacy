package io.zeebe.camel.processor

import org.apache.camel.Message

class DeployFromFileProcessor : MessageProcessor {

  override fun process(msg: Message): MessageProcessor.OutMessage {
    return MessageProcessor.OutMessage()
  }
}

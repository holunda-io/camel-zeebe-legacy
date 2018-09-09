package io.zeebe.camel.processor

import org.apache.camel.Exchange
import org.apache.camel.Message
import org.apache.camel.Processor

interface MessageProcessor : Processor {

  data class OutMessage(val headers: Map<String,Any> = mutableMapOf(), val body:Any? = null)

  override fun process(exchange: Exchange) {
    val out = process(exchange.`in`)

    exchange.out.headers.putAll(out.headers)
    exchange.out.body = out.body
  }

  fun process(msg: Message) : OutMessage
}

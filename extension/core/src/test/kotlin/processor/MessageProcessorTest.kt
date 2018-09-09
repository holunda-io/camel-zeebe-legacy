package io.zeebe.camel.processor

import org.apache.camel.Message
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.impl.DefaultExchange
import org.junit.Test
import org.assertj.core.api.Assertions.assertThat

class MessageProcessorTest {

  private val camel = DefaultCamelContext()
  private val exchange = DefaultExchange(camel)

  @Test
  fun `write square`() {
    exchange.`in`.body = 5L
    exchange.`in`.setHeader("sum", 10L)

    val processor = object : MessageProcessor {
      override fun process(msg: Message): MessageProcessor.OutMessage {
        val num = msg.getBody(Long::class.java)
        val sum  = msg.getHeader("sum", Long::class.java)

        return MessageProcessor.OutMessage(body = num * num, headers = mutableMapOf("sum" to sum + num))
      }
    }

    processor.process(exchange)

    assertThat(exchange.out.body).isEqualTo(25L)
    assertThat(exchange.out.getHeader("sum")).isEqualTo(15L)
  }
}

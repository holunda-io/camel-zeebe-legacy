package io.zeebe.camel.spike

import io.zeebe.camel.converter.JobEventMessageConverter
import org.apache.camel.Converter
import org.apache.camel.Message
import org.apache.camel.TypeConverter
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.impl.DefaultExchange
import org.junit.Test

class MessageSpike {


  @Test
  fun `do it`() {
    val camel = DefaultCamelContext()
    val ex = DefaultExchange(camel)


    val message = ex.`in`
    message.setHeader("foo", 1L)
    message.setBody("world", String::class.java)


    val mTyped = ex.getIn(JobEventMessageConverter.FooMessage::class.java)

    println(ex.`in`.getHeader("foo"))
    println(ex.`in`.body)
    println("m: $mTyped")

  }
}

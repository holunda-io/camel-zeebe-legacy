package io.zeebe.camel.converter

import org.apache.camel.Converter
import org.apache.camel.Message

@Converter
class JobEventMessageConverter {

  data class FooMessage(val foo: Long, val hello:String)

  @Converter
  class MessageConverter {

    @Converter
    fun convert(msg: Message)  = FooMessage(msg.getHeader("foo") as Long, msg.getBody(String::class.java))
  }


}

package io.zeebe.camel.api.command

import org.apache.camel.Produce
import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
import org.junit.Test


class SendDeployCommandTest {

  private val context = DefaultCamelContext()

  class DeployCommandProducer {

    //@Produce(uri= "direct:deploy")
    lateinit var template: ProducerTemplate

    fun send(): DeployCommand = DeployCommand("hello", "theResource", "topic")

  }


  @Test
  fun `publish deployCommand to route`() {
    context.addRoutes(object: RouteBuilder(){
      override fun configure() {
        from("direct:deploy")
            .to("log:bean")
      }
    })

    context.start();


  }
}

package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.CompleteJobCommand
import io.zeebe.camel.endpoint.ZeebeProducerOnlyEndpoint
import io.zeebe.camel.jobEvent
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint

@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe Complete Job",
    syntax = JobCompleteEndpoint.ENDPOINT,
    producerOnly = true
)
class JobCompleteEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, JobCompleteEndpoint.ENDPOINT) {

  companion object {
    const val REMAINING = "job/complete"
    const val ENDPOINT = "${ZeebeComponent.SCHEME}:$REMAINING"
  }


  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val cmd = exchange.`in`.getMandatoryBody(CompleteJobCommand::class.java)

      val jobEvent = zeebeObjectMapper.jobEvent(cmd.jobEventJson)

      val builder = context.jobClient
          .newCompleteCommand(jobEvent)

      val payload = cmd.payload

      if (payload != null ) {
        builder.payload(payload)
      }

      builder.send().join()
    }
  }
}

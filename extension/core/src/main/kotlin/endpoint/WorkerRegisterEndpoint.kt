package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.RegisterJobWorkerCommand
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint

@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe register worker",
    syntax = WorkerRegisterEndpoint.ENDPOINT,
    producerOnly = true
)
class WorkerRegisterEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, WorkerRegisterEndpoint.ENDPOINT) {

  companion object {
    const val REMAINING = "worker/register"
    const val ENDPOINT = "${ZeebeComponent.SCHEME}:$REMAINING"
  }

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val cmd = exchange.`in`.getMandatoryBody(RegisterJobWorkerCommand::class.java)

      exchange.context.addRoutes(object: RouteBuilder() {
        override fun configure() {
          from(JobSubscribeEndpoint.endpoint(cmd.jobType, cmd.workerName))
              .routeId(cmd.toString())
              .to(cmd.to)
        }
      })
    }
  }
}

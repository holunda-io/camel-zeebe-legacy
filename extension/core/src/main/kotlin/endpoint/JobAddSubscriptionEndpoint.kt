package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.AddSubscriptionCommand
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint

@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe Add Subscription",
    syntax = JobAddSubscriptionEndpoint.SYNTAX,
    producerOnly = true
)
class JobAddSubscriptionEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, JobAddSubscriptionEndpoint.SYNTAX) {

  companion object {
    const val COMMAND = "job/addSubscription"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"
  }

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val cmd = exchange.`in`.getMandatoryBody(AddSubscriptionCommand::class.java)

      exchange.context.addRoutes(object: RouteBuilder() {
        override fun configure() {
          from(JobSubscribeEndpoint.from(cmd.jobType, cmd.workerName))
              .routeId(cmd.toString())
              .to(cmd.to)
        }
      })
    }
  }
}

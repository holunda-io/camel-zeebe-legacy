package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.AddSubscriptionCommand
import io.zeebe.camel.api.command.DeployCommand
import io.zeebe.camel.message.ProcessDeployMessage
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.RoutesBuilder
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.model.RouteDefinition
import org.apache.camel.spi.UriEndpoint
import org.slf4j.LoggerFactory

@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe Deployment",
    syntax = ProcessDeployEndpoint.SYNTAX,
    producerOnly = true
)
class JobAddSubscriptionEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, ProcessDeployEndpoint.SYNTAX) {

  companion object {
    const val COMMAND = "job/addSubscription"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"

    val logger = LoggerFactory.getLogger(ProcessDeployEndpoint::class.java)!!
  }

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val cmd = exchange.`in`.getBody(AddSubscriptionCommand::class.java)

      exchange.context.addRoutes(object: RouteBuilder() {
        override fun configure() {
          from(JobSubscribeEndpoint.from(cmd))
              .routeId(cmd.toString())
              .to(cmd.to)
        }

      })

      logger.info("deployed: resource={}", cmd)
    }
  }
}

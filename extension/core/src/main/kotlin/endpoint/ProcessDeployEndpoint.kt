package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.DeployProcessCommand
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint
import org.slf4j.LoggerFactory

@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe Deployment",
    syntax = ProcessDeployEndpoint.ENDPOINT,
    producerOnly = true
)
class ProcessDeployEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, ProcessDeployEndpoint.ENDPOINT) {

  companion object {
    const val REMAINING = "process/deploy"
    const val ENDPOINT = "${ZeebeComponent.SCHEME}:$REMAINING"

    val logger = LoggerFactory.getLogger(ProcessDeployEndpoint::class.java)!!
  }

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val cmd = exchange.`in`.getMandatoryBody(DeployProcessCommand::class.java)

      context.workflowClient
          .newDeployCommand()
          .addResourceStringUtf8(cmd.xml, cmd.name)
          .send()
          .join()

      logger.info("deployed: resource={}", cmd)
    }
  }
}

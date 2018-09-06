package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.DeployCommand
import mu.KLogging
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint

@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe Deployment",
    syntax = ProcessDeployEndpoint.SYNTAX,
    producerOnly = true
)
class ProcessDeployEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, ProcessDeployEndpoint.SYNTAX) {

  companion object : KLogging() {
    const val COMMAND = "process/deploy"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"
  }

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val cmd = exchange.getIn().getBody(DeployCommand::class.java)

      context.workflowClient
          .newDeployCommand()
          .addResourceStringUtf8(cmd.xml, cmd.name)
          .send()
          .join()

      logger.info { "deployed: resource=$cmd" }
    }
  }
}

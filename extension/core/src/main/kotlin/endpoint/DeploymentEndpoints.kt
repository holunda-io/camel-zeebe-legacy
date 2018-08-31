package io.zeebe.camel.endpoint

import io.zeebe.camel.api.command.DeployCommand
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.ZeebeComponent
import mu.KLogging
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint

@UriEndpoint(
    scheme = ZeebeComponent.SCHEME, title = "Zeebe Deployment", syntax = DeployModelEndpoint.SYNTAX,
    producerOnly = true
)
class DeployModelEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context) {

  companion object : KLogging() {
    const val COMMAND = "deployment"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"
  }

  override fun getSyntax() = SYNTAX


  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val cmd = exchange.getIn().getBody(DeployCommand::class.java)

      context.topicClient(cmd.topic)
          .workflowClient()
          .newDeployCommand()
          .addResourceStringUtf8(cmd.bpmnResource, cmd.resourceName)
          .send()
          .join()

      logger.info { "deployed: topic=${cmd.topic}, resource=${cmd.resourceName}" }
    }

  }

}

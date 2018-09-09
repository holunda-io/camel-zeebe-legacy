package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.DeployCommand
import io.zeebe.camel.message.ProcessDeployMessage
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint
import org.slf4j.LoggerFactory

@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe Deployment",
    syntax = ProcessDeployEndpoint.SYNTAX,
    producerOnly = true
)
class ProcessDeployEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, ProcessDeployEndpoint.SYNTAX) {

  companion object {
    const val COMMAND = "process/deploy"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"

    val logger = LoggerFactory.getLogger(ProcessDeployEndpoint::class.java)!!
  }

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {

      val msg = exchange.`in`
      val cmd =
          if (msg is ProcessDeployMessage)
            exchange.getIn(ProcessDeployMessage::class.java).toCommand()
          else {
            if (msg.body is DeployCommand) msg.getBody(DeployCommand::class.java)
            else throw IllegalArgumentException("neither message nor body match.")
          }

      context.workflowClient
          .newDeployCommand()
          .addResourceStringUtf8(cmd.xml, cmd.name)
          .send()
          .join()

      logger.info("deployed: resource={}", cmd)
    }
  }
}

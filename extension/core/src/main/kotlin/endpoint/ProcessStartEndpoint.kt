package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.StartProcessCommand
import mu.KLogging
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint


@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe Start Process",
    syntax = ProcessStartEndpoint.SYNTAX,
    producerOnly = true
)
class ProcessStartEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, ProcessStartEndpoint.SYNTAX) {

  companion object : KLogging() {
    const val COMMAND = "process/start"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"
  }

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val cmd = exchange.getIn().getBody(StartProcessCommand::class.java)

      context.workflowClient
          .newCreateInstanceCommand()
          .bpmnProcessId(cmd.bpmnProcessId)
          .latestVersion()
          .payload(cmd.payload)
          .send()
          .join()

      logger.info { "started: bpmnProcessId=${cmd.bpmnProcessId}" }
    }
  }
}

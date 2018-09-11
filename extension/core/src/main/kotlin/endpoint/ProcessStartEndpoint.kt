package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.StartProcessCommand
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint
import org.slf4j.LoggerFactory


@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe Start Process",
    syntax = ProcessStartEndpoint.SYNTAX,
    producerOnly = true
)
class ProcessStartEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, ProcessStartEndpoint.SYNTAX) {

  companion object {
    const val COMMAND = "process/start"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"
    val logger = LoggerFactory.getLogger(ProcessStartEndpoint::class.java)!!
  }

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val cmd = exchange.getIn().getMandatoryBody(StartProcessCommand::class.java)

      context.workflowClient
          .newCreateInstanceCommand()
          .bpmnProcessId(cmd.bpmnProcessId)
          .latestVersion()
          .payload(cmd.payload)
          .send()
          .join()

      logger.info("started: bpmnProcessId={}", cmd.bpmnProcessId )
    }
  }
}

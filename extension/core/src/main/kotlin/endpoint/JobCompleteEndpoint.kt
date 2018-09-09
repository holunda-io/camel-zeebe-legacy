package endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.CompleteJobCommand
import io.zeebe.camel.endpoint.ZeebeProducerOnlyEndpoint
import io.zeebe.camel.processor.InitJobEventProcessor
import io.zeebe.camel.processor.PrepareCompleteJobCommandProcessor
import org.apache.camel.Exchange
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint
import org.apache.camel.spi.UriParam

@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe Complete Job",
    syntax = JobCompleteEndpoint.SYNTAX,
    producerOnly = true
)
class JobCompleteEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, JobCompleteEndpoint.SYNTAX) {

  companion object {
    const val COMMAND = "job/complete"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"
  }

  @UriParam(name = "fromJson", label = "if the complete job command should be build from header/json")
  @org.apache.camel.spi.Metadata(required = "false")
  var fromJson: Boolean = false


  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      if (fromJson) {
        PrepareCompleteJobCommandProcessor(context.objectMapper).process(exchange)
      }

      val message = exchange.`in`

      val command = message.getBody(CompleteJobCommand::class.java)

      val jobEvent = context.jobEvent(command.jobEventJson)

      val builder = context.jobClient
          .newCompleteCommand(jobEvent)

      val payload = message.getBody(String::class.java)

      if (payload != null ) {
        builder.payload(payload)
      }

      builder.send().join()
    }
  }
}

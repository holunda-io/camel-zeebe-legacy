package endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.CompleteJobCommand
import io.zeebe.camel.api.command.DeployCommand
import io.zeebe.camel.endpoint.ZeebeProducerOnlyEndpoint
import io.zeebe.camel.message.JobCompleteMessage
import io.zeebe.camel.message.ProcessDeployMessage
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


  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val msg = JobCompleteMessage(exchange.`in` )


      val jobEvent = context.jobEvent(msg.getJobEventJson())

      val builder = context.jobClient
          .newCompleteCommand(jobEvent)

      val payload = msg.body

      if (payload != null ) {
        builder.payload(payload)
      }

      builder.send().join()
    }
  }
}

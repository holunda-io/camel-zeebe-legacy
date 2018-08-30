package io.zeebe.camel.jobworker

import io.zeebe.camel.CreateEndpoint
import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.jobworker.CompleteJobEndpoint.Companion.SYNTAX
import io.zeebe.client.ZeebeClient
import io.zeebe.client.api.events.JobEvent
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultEndpoint
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint
import java.util.function.Supplier

@UriEndpoint(
    scheme = ZeebeComponent.SCHEME, title = "Zeebe Complete Job", syntax = SYNTAX,
    producerOnly = true
)
class CompleteJobEndpoint(val client: Supplier<ZeebeClient>, val createEndpoint: CreateEndpoint) : DefaultEndpoint() {

  companion object {
    const val COMMAND = "completejob"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"
  }

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val jobEvent = exchange.getIn().getBody(JobEvent::class.java)

      client.get()
          .topicClient()
          .jobClient()
          .newCompleteCommand(jobEvent)
          .send().join()
    }
  }

  override fun createConsumer(processor: Processor) = throw UnsupportedOperationException("${SYNTAX} - producerOnly")
  override fun isSingleton() = true
  override fun createEndpointUri() = createEndpoint.uri
}

package io.zeebe.camel.endpoint

import io.zeebe.camel.CamelZeebeContext
import io.zeebe.camel.ZeebeComponent
import io.zeebe.client.ZeebeClient
import io.zeebe.client.api.events.JobEvent
import io.zeebe.client.api.subscription.JobWorker
import mu.KLogging
import org.apache.camel.Consumer
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultConsumer
import org.apache.camel.impl.DefaultEndpoint
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint
import org.apache.camel.spi.UriParam
import java.util.function.Supplier


@UriEndpoint(
    scheme = ZeebeComponent.SCHEME, title = "Zeebe Complete Job", syntax = CompleteJobEndpoint.SYNTAX,
    producerOnly = true
)
class CompleteJobEndpoint(context: CamelZeebeContext) : ZeebeProducerOnlyEndpoint(context) {

  companion object {
    const val COMMAND = "completejob"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"
  }

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val jobEvent = exchange.getIn().getBody(JobEvent::class.java)

      context.topicClient()
          .jobClient()
          .newCompleteCommand(jobEvent)
          .send().join()
    }
  }

  override fun getSyntax(): String = SYNTAX

}



@UriEndpoint(
    scheme = ZeebeComponent.SCHEME, title = "Zeebe Subscribe JobWorker", syntax = SubscribeJobWorkerEndpoint.SYNTAX,
    consumerOnly = true
)
class SubscribeJobWorkerEndpoint(context: CamelZeebeContext) : ZeebeConsumerOnlyEndpoint(context) {

  companion object : KLogging() {
    const val COMMAND = "jobworker"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"
  }

  @UriParam(name = "topic", label = "The topic to subscribe to")
  @org.apache.camel.spi.Metadata(required = "true")
  lateinit var topic: String

  override fun createConsumer(processor: Processor): Consumer = object : DefaultConsumer(this, processor) {

    lateinit var jobWorker: JobWorker

    override fun doStart() {
      jobWorker = context.topicClient()
          .jobClient()
          .newWorker()
          .jobType("doSomething")
          .handler { _, job ->
            with(endpoint.createExchange()) {
              getIn().body = job
              processor.process(this)
            }
          }
          .name("jobWorkerEndpoint")
          .open()
    }

    override fun doStop() {
      if (!jobWorker.isClosed)
        jobWorker.close()
    }
  }
  override fun getSyntax(): String = CompleteJobEndpoint.SYNTAX

}

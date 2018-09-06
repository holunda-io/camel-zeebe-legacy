package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.CompleteJobCommand
import io.zeebe.client.api.subscription.JobWorker
import mu.KLogging
import org.apache.camel.Consumer
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultConsumer
import org.apache.camel.impl.DefaultProducer
import org.apache.camel.spi.UriEndpoint
import org.apache.camel.spi.UriParam


@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe Complete Job",
    syntax = CompleteJobEndpoint.SYNTAX,
    producerOnly = true
)
class CompleteJobEndpoint(context: ZeebeComponentContext) : ZeebeProducerOnlyEndpoint(context, CompleteJobEndpoint.SYNTAX) {

  companion object {
    const val COMMAND = "complete-job"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"
  }

  override fun createProducer(): Producer = object : DefaultProducer(this) {
    override fun process(exchange: Exchange) {
      val command = exchange.getIn().getBody(CompleteJobCommand::class.java)

      val builder = context.jobClient
          .newCompleteCommand(context.jobEvent(command.jobEventJson.json))

      command.payload.let { builder.payload(it) }

      builder.send().join()
    }
  }
}


@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe Subscribe JobWorker",
    syntax = SubscribeJobWorkerEndpoint.SYNTAX,
    consumerOnly = true
)
class SubscribeJobWorkerEndpoint(context: ZeebeComponentContext) : ZeebeConsumerOnlyEndpoint(context, SubscribeJobWorkerEndpoint.SYNTAX) {

  companion object : KLogging() {
    const val COMMAND = "jobworker"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"
  }

  override fun createConsumer(processor: Processor): Consumer = object : DefaultConsumer(this, processor) {

    lateinit var jobWorker: JobWorker

    override fun doStart() {
      jobWorker = context.jobClient
          .newWorker()
          .jobType("doSomething") // FIXME
          .handler { _, job ->
            val event = io.zeebe.camel.api.event.JobEvent(context.jobEvent(job))
            with(endpoint.createExchange()) {
              getIn().body = event
              processor.process(this)
            }
          }
          .name("jobWorkerEndpoint") // FIXME
          .open()
    }

    override fun doStop() {
      if (!jobWorker.isClosed)
        jobWorker.close()
    }
  }
}

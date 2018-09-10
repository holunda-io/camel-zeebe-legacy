package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.command.AddSubscriptionCommand
import io.zeebe.camel.jobEvent
import io.zeebe.camel.message.JobCompleteMessage
import io.zeebe.camel.processor.InitJobEventProcessor
import io.zeebe.client.api.record.ZeebeObjectMapper
import io.zeebe.client.api.subscription.JobWorker
import org.apache.camel.Consumer
import org.apache.camel.Processor
import org.apache.camel.impl.DefaultConsumer
import org.apache.camel.spi.UriEndpoint
import org.apache.camel.spi.UriParam

@UriEndpoint(
    scheme = ZeebeComponent.SCHEME,
    title = "Zeebe Subscribe JobWorker",
    syntax = JobSubscribeEndpoint.SYNTAX,
    consumerOnly = true
)
class JobSubscribeEndpoint(context: ZeebeComponentContext) : ZeebeConsumerOnlyEndpoint(context, JobSubscribeEndpoint.SYNTAX) {

  companion object  {
    const val COMMAND = "job/subscribe"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"

    fun from(cmd: AddSubscriptionCommand) = "${ZeebeComponent.SCHEME}:$COMMAND?jobType=${cmd.jobType}&workerName=${cmd.workerName}"
  }

  private val objectMapper : ZeebeObjectMapper by lazy {
    context.objectMapper
  }

  @UriParam(name = "jobType", label = "the type of job to subscribe to")
  @org.apache.camel.spi.Metadata(required = "true")
  lateinit var jobType: String

  @UriParam(name = "workerName", label = "the name of the worker")
  @org.apache.camel.spi.Metadata(required = "false")
  var workerName: String? = null

  override fun createConsumer(processor: Processor): Consumer = object : DefaultConsumer(this, processor) {


    lateinit var jobWorker: JobWorker

    override fun doStart() {
      val builder = context.jobClient
          .newWorker()
          .jobType(jobType)
          .handler { _, job ->
            val exchange = endpoint.createExchange()

            exchange.`in` = JobCompleteMessage(exchange.context)
                .withJobType(jobType)
                .withPayload(job.payload)
                .withJobWorker(workerName)
                .withJobEvent(objectMapper.jobEvent(job))

              processor.process(exchange)
          }

      if (workerName != null) {
        builder.name(workerName)
      }

      jobWorker = builder.open()
    }

    override fun doStop() {
      if (!jobWorker.isClosed)
        jobWorker.close()
    }
  }
}

package io.zeebe.camel.endpoint

import io.zeebe.camel.UriMetadata
import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.client.api.subscription.JobWorker
import mu.KLogging
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

  companion object : KLogging() {
    const val COMMAND = "job/subscribe"
    const val SYNTAX = "${ZeebeComponent.SCHEME}:$COMMAND"
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
            val event = io.zeebe.camel.api.event.JobEvent(context.jobEvent(job))
            with(endpoint.createExchange()) {
              getIn().body = event
              processor.process(this)
            }
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

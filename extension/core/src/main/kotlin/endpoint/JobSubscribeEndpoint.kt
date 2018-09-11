package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponent
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.camel.api.event.JobCreatedEvent
import io.zeebe.camel.jobEvent
import io.zeebe.client.api.record.ZeebeObjectMapper
import io.zeebe.client.api.subscription.JobWorker
import org.apache.camel.Consumer
import org.apache.camel.Processor
import org.apache.camel.impl.DefaultConsumer
import org.apache.camel.spi.UriEndpoint
import org.apache.camel.spi.UriParam
import java.time.Duration
import java.time.temporal.ChronoUnit

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

    fun from(jobType: String, workerName: String) = "${ZeebeComponent.SCHEME}:$COMMAND" +
        "?jobType=$jobType" +
        "&workerName=$workerName"
  }


  @UriParam(name = "jobType", label = "the type of job to subscribe to")
  @org.apache.camel.spi.Metadata(required = "true")
  lateinit var jobType: String

  @UriParam(name = "workerName", label = "the name of the worker")
  @org.apache.camel.spi.Metadata(required = "false")
  lateinit var workerName: String

  override fun createConsumer(processor: Processor): Consumer = object : DefaultConsumer(this, processor) {

    lateinit var jobWorker: JobWorker

    override fun doStart() {
      jobWorker = context.jobClient
          .newWorker()
          .jobType(jobType)
          .handler { _, job ->
            val exchange = endpoint.createExchange()
            exchange.`in`.body = objectMapper.writeValueAsString(JobCreatedEvent(
                jobType = jobType,
                workerName = workerName,
                jobEvent = zeebeObjectMapper.jobEvent(job),
                payload = job.payload
            ))

            processor.process(exchange)
          }.name(workerName)
          .timeout(Duration.of(10, ChronoUnit.MINUTES))
          .open()
    }

    override fun doStop() {
      if (!jobWorker.isClosed)
        jobWorker.close()
    }
  }
}

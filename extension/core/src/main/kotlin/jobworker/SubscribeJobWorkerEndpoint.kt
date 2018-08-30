package io.zeebe.camel.jobworker

import io.zeebe.camel.CreateEndpoint
import io.zeebe.camel.ZeebeComponent
import io.zeebe.client.ZeebeClient
import io.zeebe.client.api.subscription.JobWorker
import mu.KLogging
import org.apache.camel.Consumer
import org.apache.camel.Processor
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultConsumer
import org.apache.camel.impl.DefaultEndpoint
import org.apache.camel.spi.UriEndpoint
import java.util.function.Supplier
import org.apache.camel.spi.UriParam




@UriEndpoint(
    scheme = ZeebeComponent.SCHEME, title = "Zeebe Subscribe JobWorker", syntax = SubscribeJobWorkerEndpoint.SYNTAX,
    consumerOnly = true
)
class SubscribeJobWorkerEndpoint(val client: Supplier<ZeebeClient>, val createEndpoint: CreateEndpoint) : DefaultEndpoint() {

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
      jobWorker = client.get().topicClient()
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

  override fun createProducer(): Producer = throw UnsupportedOperationException("${SYNTAX} - consumerOnly")
  override fun isSingleton() = true
  override fun createEndpointUri(): String = createEndpoint.uri

  override fun toString() = "SubscribeJobWorkerEndpoint(topic='$topic')"


}


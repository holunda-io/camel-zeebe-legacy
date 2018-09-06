package io.zeebe.camel

import io.zeebe.camel.endpoint.CompleteJobEndpoint
import io.zeebe.camel.endpoint.DeployModelEndpoint
import io.zeebe.camel.endpoint.StartProcessEndpoint
import io.zeebe.camel.endpoint.SubscribeJobWorkerEndpoint
import io.zeebe.client.ZeebeClient
import io.zeebe.client.api.clients.TopicClient
import io.zeebe.client.api.events.JobEvent
import io.zeebe.client.impl.ZeebeClientImpl
import io.zeebe.client.impl.data.ZeebeObjectMapperImpl
import mu.KLogging
import org.apache.camel.Endpoint
import org.apache.camel.impl.DefaultComponent
import java.util.function.Supplier

/**
 * The zeebe core component. It is identified by camel via the {@link ZeebeComponent#SCHEME}
 * keyword. To get registered, this class is bound to the scheme in <code>/META-INF/services/org/apache/camel/component/zeebe</code>.
 */
class ZeebeComponent(private val clientSupplier: Supplier<ZeebeClient>) : DefaultComponent() {

  constructor(client: ZeebeClient) : this(Supplier { client })

  companion object : KLogging() {
    const val SCHEME = "zeebe"
  }

  private fun createEndpoint(context: ZeebeComponentContext): Endpoint = when (context.remaining) {
    CompleteJobEndpoint.COMMAND -> CompleteJobEndpoint(context)
    DeployModelEndpoint.COMMAND -> DeployModelEndpoint(context)
    SubscribeJobWorkerEndpoint.COMMAND -> SubscribeJobWorkerEndpoint(context)
    StartProcessEndpoint.COMMAND -> StartProcessEndpoint(context)
    else -> throw IllegalArgumentException("unkown: ${context.remaining}")
  }

  override fun createEndpoint(uri: String, remaining: String, parameters: MutableMap<String, Any>): Endpoint = createEndpoint(ZeebeComponentContext(uri, remaining, parameters, clientSupplier))
}

/**
 * The component context stores all endpoint parameters passed via dsl and provides access to the
 * ZeebeClient functions.
 */
data class ZeebeComponentContext(
    val uri: String,
    val remaining: String,
    val parameters: Map<String, Any>,
    val clientSupplier: Supplier<ZeebeClient>
) {
  fun topicClient(topicName : String? = null): TopicClient = with(clientSupplier.get()) {
    if (topicName == null)
      return topicClient()
    else return topicClient(topicName)
  }

  fun objectMapper(): ZeebeObjectMapperImpl {
    val client = clientSupplier.get()
    return if (client is ZeebeClientImpl)
      client.objectMapper
    else
      ZeebeObjectMapperImpl()
  }

  fun jobEvent(event: JobEvent) = objectMapper().toJson(event)!!
  fun jobEvent(json: String) = objectMapper().fromJson(json, JobEvent::class.java)!!
}

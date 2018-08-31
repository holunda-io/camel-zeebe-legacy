package io.zeebe.camel

import io.zeebe.camel.endpoint.CompleteJobEndpoint
import io.zeebe.camel.endpoint.SubscribeJobWorkerEndpoint
import io.zeebe.client.ZeebeClient
import io.zeebe.client.api.clients.TopicClient
import mu.KLogging
import org.apache.camel.Endpoint
import org.apache.camel.impl.DefaultComponent
import java.util.function.Supplier

/**
 * The zeebe core component. It is identified by camel via the {@link ZeebeComponent#SCHEME}
 * keyword. To get registered, this class is bound to the scheme in <code>/META-INF/services/org/apache/camel/component/zeebe</code>.
 */
class ZeebeComponent(val clientSupplier: Supplier<ZeebeClient>) : DefaultComponent() {

  constructor(client: ZeebeClient) : this(Supplier { client })

  companion object : KLogging() {
    const val SCHEME = "zeebe"
  }

  fun createEndpoint(context: CamelZeebeContext): Endpoint = when (context.remaining) {
    CompleteJobEndpoint.COMMAND -> CompleteJobEndpoint(context)
    SubscribeJobWorkerEndpoint.COMMAND -> SubscribeJobWorkerEndpoint(context)
    else -> throw IllegalArgumentException("unkown: ${context.remaining}")
  }

  override fun createEndpoint(uri: String, remaining: String, parameters: MutableMap<String, Any>): Endpoint = createEndpoint(CamelZeebeContext(uri, remaining, parameters, clientSupplier))

}

data class CamelZeebeContext(
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
}

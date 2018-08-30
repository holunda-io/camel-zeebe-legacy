package io.zeebe.camel

import io.zeebe.camel.jobworker.CompleteJobEndpoint
import io.zeebe.camel.jobworker.SubscribeJobWorkerEndpoint
import io.zeebe.client.ZeebeClient
import mu.KLogging
import org.apache.camel.Endpoint
import org.apache.camel.impl.DefaultComponent
import java.util.function.Supplier

/**
 * The zeebe core component. It is identified by camel via the {@link ZeebeComponent#SCHEME}
 * keyword. To get registered, this class is bound to the scheme in <code>/META-INF/services/org/apache/camel/component/zeebe</code>.
 */
class ZeebeComponent(val client: Supplier<ZeebeClient>) : DefaultComponent() {

  constructor(client: ZeebeClient) : this(Supplier { client })

  companion object : KLogging() {
    const val SCHEME = "zeebe"
  }

  override fun createEndpoint(uri: String, remaining: String, parameters: MutableMap<String, Any>): Endpoint {
    val createEndpoint = CreateEndpoint(uri, remaining, parameters)

    logger.info { "createEndpoint: $createEndpoint" }

    return when (remaining) {
      CompleteJobEndpoint.COMMAND -> CompleteJobEndpoint(client, createEndpoint)
      SubscribeJobWorkerEndpoint.COMMAND -> SubscribeJobWorkerEndpoint(client, createEndpoint)
      else -> throw IllegalArgumentException("unkown: $remaining")
    }
  }

}

data class CreateEndpoint(
    val uri: String,
    val remaining: String,
    val parameters: Map<String, Any>
)

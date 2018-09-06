package io.zeebe.camel.endpoint

import io.zeebe.camel.ZeebeComponentContext
import org.apache.camel.Consumer
import org.apache.camel.Processor
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultEndpoint

abstract class ZeebeEndpoint(
    val context: ZeebeComponentContext,
    val syntax: String) : DefaultEndpoint() {
  abstract override fun createConsumer(processor: Processor): Consumer
  abstract override fun createProducer(): Producer

  override fun isSingleton(): Boolean = true
  override fun createEndpointUri() = context.uri

}

abstract class ZeebeConsumerOnlyEndpoint(context: ZeebeComponentContext, syntax:String) : ZeebeEndpoint(context, syntax) {
  override fun createProducer(): Producer = throw UnsupportedOperationException("$syntax is consumerOnly!")
}

abstract class ZeebeProducerOnlyEndpoint(context: ZeebeComponentContext, syntax:String) : ZeebeEndpoint(context, syntax) {
  override fun createConsumer(processor: Processor) = throw UnsupportedOperationException("$syntax} is producerOnly!")
}

package io.zeebe.camel.endpoint

import io.zeebe.camel.CamelZeebeContext
import org.apache.camel.Consumer
import org.apache.camel.Processor
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultEndpoint

abstract class ZeebeEndpoint(val context: CamelZeebeContext) : DefaultEndpoint() {
  abstract override fun createConsumer(processor: Processor): Consumer
  abstract override fun createProducer(): Producer
  abstract fun getSyntax() : String

  override fun isSingleton(): Boolean = true
  override fun createEndpointUri() = context.uri

}

abstract class ZeebeConsumerOnlyEndpoint(context: CamelZeebeContext) : ZeebeEndpoint(context) {
  override fun createProducer(): Producer = throw UnsupportedOperationException("${getSyntax()} is consumerOnly!")
}

abstract class ZeebeProducerOnlyEndpoint(context: CamelZeebeContext) : ZeebeEndpoint(context) {
  override fun createConsumer(processor: Processor) = throw UnsupportedOperationException("${getSyntax()} is producerOnly!")
}

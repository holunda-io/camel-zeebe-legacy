package io.zeebe.camel.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.zeebe.camel.ZeebeComponentContext
import io.zeebe.client.api.record.ZeebeObjectMapper
import org.apache.camel.Consumer
import org.apache.camel.Processor
import org.apache.camel.Producer
import org.apache.camel.impl.DefaultEndpoint

abstract class ZeebeEndpoint(
    val context: ZeebeComponentContext,
    val syntax: String
) : DefaultEndpoint() {
  abstract override fun createConsumer(processor: Processor): Consumer
  abstract override fun createProducer(): Producer

  val objectMapper : ObjectMapper = jacksonObjectMapper()
  val zeebeObjectMapper : ZeebeObjectMapper by lazy {
    context.objectMapper
  }

  override fun isSingleton(): Boolean = true
  override fun createEndpointUri() = context.uri
}

abstract class ZeebeConsumerOnlyEndpoint(
    context: ZeebeComponentContext,
    syntax: String
) : ZeebeEndpoint(context, syntax) {
  override fun createProducer() = throw UnsupportedOperationException("$syntax is consumerOnly!")
}

abstract class ZeebeProducerOnlyEndpoint(
    context: ZeebeComponentContext,
    syntax: String
) : ZeebeEndpoint(context, syntax) {
  override fun createConsumer(processor: Processor) = throw UnsupportedOperationException("$syntax} is producerOnly!")
}

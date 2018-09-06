@file:Suppress(PACKAGE)
package io.zeebe.camel.api.processor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.zeebe.camel.api.kotlin.Json
import io.zeebe.camel.api.kotlin.PACKAGE
import org.apache.camel.Exchange
import kotlin.reflect.KClass

class JobWorkerProcessor {
}

abstract class JobWorkerExchange<I : Any,O : Any>(
        val exchange: Exchange,
        val payloadIn: KClass<I>,
        val payloadOut: KClass<O>,
        val objectMapper : ObjectMapper = jacksonObjectMapper()
) : Exchange by exchange {

    fun getPayload() : I = objectMapper.convertValue<I>(`in`.getBody(payloadIn.java), payloadIn.java)
    fun setPayload(payload: O) = `in`.setBody(objectMapper.writeValueAsString(payload), Json::class.java)
}

@file:Suppress("PackageDirectoryMismatch")
package io.zeebe.camel.processor

import io.zeebe.camel.Json
import io.zeebe.camel.jobEvent
import io.zeebe.client.api.events.JobEvent
import io.zeebe.client.api.record.ZeebeObjectMapper
import org.apache.camel.Exchange
import org.apache.camel.Processor


class JobEventToJson(private val objectMapper : ZeebeObjectMapper) : Processor {
    override fun process(exchange: Exchange) = with(exchange.`in`) {

        val jobEvent = getBody(JobEvent::class.java)

        setHeader("jobEvent", objectMapper.jobEvent(jobEvent))

        setBody(jobEvent.payload, Json::class.java)
    }
}


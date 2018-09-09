package io.zeebe.camel

import io.zeebe.camel.api.Json
import io.zeebe.client.api.events.JobEvent
import io.zeebe.client.api.record.ZeebeObjectMapper
import org.apache.camel.Exchange
import org.apache.camel.Message
import org.apache.camel.Processor
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

fun ZeebeObjectMapper.jobEvent(jobEvent: JobEvent) : Json = this.toJson(jobEvent)
fun ZeebeObjectMapper.jobEvent(json: Json) : JobEvent = this.fromJson(json, JobEvent::class.java)

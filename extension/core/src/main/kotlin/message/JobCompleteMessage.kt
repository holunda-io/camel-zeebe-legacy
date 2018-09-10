package io.zeebe.camel.message

import io.zeebe.camel.api.Json
import io.zeebe.camel.api.command.CompleteJobCommand
import org.apache.camel.CamelContext
import org.apache.camel.Message
import org.apache.camel.component.file.GenericFileMessage
import org.apache.camel.impl.DefaultMessage
import java.io.File

class JobCompleteMessage(context: CamelContext) : DefaultMessage(context) {

  companion object {
    const val HEADER_JOB_EVENT = "zeebeJobEvent"
    const val HEADER_JOB_TYPE = "zeebeJobType"
    const val HEADER_JOB_WORKER = "zeebeJobWorker"
  }

  constructor(msg: Message) : this(msg.exchange.context) {
    headers.putAll(msg.headers)
    body = msg.body
  }


  constructor(context: CamelContext, jobEvent: Json, payload: Json?) : this(context) {
    withJobEvent(jobEvent)
    withPayload(payload)
  }

  fun toCommand() = CompleteJobCommand(getHeader(HEADER_JOB_EVENT) as Json, getBody(Json::class.java))


  fun getJobEventJson() = getHeader(HEADER_JOB_EVENT) as Json

  fun withJobEvent(jobEvent: Json) : JobCompleteMessage {
    setHeader(HEADER_JOB_EVENT, jobEvent)
    return this
  }

  fun withPayload(payload: Json?): JobCompleteMessage {
    setBody(payload, Json::class.java)
    return this
  }

  fun withJobType(jobType: String) : JobCompleteMessage {
    setHeader(HEADER_JOB_TYPE, jobType)
    return this
  }

  fun withJobWorker(jobWorker: String?) : JobCompleteMessage {
    setHeader(HEADER_JOB_WORKER, jobWorker)
    return this
  }
}

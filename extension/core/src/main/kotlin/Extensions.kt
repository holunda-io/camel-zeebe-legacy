package io.zeebe.camel

import io.zeebe.camel.api.Json
import io.zeebe.client.ZeebeClient
import io.zeebe.client.api.events.JobEvent
import io.zeebe.client.api.record.ZeebeObjectMapper

fun ZeebeObjectMapper.jobEvent(jobEvent: JobEvent) : Json = this.toJson(jobEvent)
fun ZeebeObjectMapper.jobEvent(json: Json) : JobEvent = this.fromJson(json, JobEvent::class.java)


fun ZeebeClient.getDeployedProcesses() = this
  .topicClient()
  .workflowClient()
  .newWorkflowRequest()
  .send()
  .join()
  .workflows
  .map { "${it.bpmnProcessId} [${it.version}]" }
  .sorted()

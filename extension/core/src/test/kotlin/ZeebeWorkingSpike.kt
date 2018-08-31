package io.zeebe.camel

import io.zeebe.client.api.clients.JobClient
import io.zeebe.client.api.events.JobEvent
import io.zeebe.client.api.events.WorkflowInstanceEvent
import io.zeebe.client.api.record.Record
import io.zeebe.test.ZeebeTestRule
import mu.KLogging
import org.apache.camel.CamelContext
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
import org.awaitility.Awaitility
import org.junit.Rule
import org.junit.Test
import java.util.*
import java.util.function.Supplier


class ZeebeWorkingSpike {

  companion object : KLogging()

  @get:Rule
  val zeebe = ZeebeTestRule()

  private val records = mutableListOf<Record>()

  @Test
  fun `without zeebe`() {
    //createTopic()
    subscribe()
    deploy()
    startWorkflow()

    zeebe.client.topicClient()
        .jobClient()
        .newWorker()
        .jobType("doSomething")
        .handler { client, job -> complete(client, job) }
        .name("jobWorkerEndpoint")
        .open()

    Awaitility.await().untilAsserted({
      records.find { it.metadata.intent == "COMPLETED" } != null
    })

    //zeebe.waitUntilJobCompleted(4294968672L)
  }

  @Test
  fun `start process and work on task`() {
    //createTopic()
    subscribe()
    deploy()


    val routeBuilder: RouteBuilder = object : RouteBuilder() {
      override fun configure() {
        from("zeebe:jobworker?topic=default-topic")
            .to("direct:foo")

        from("direct:foo")
            .to("zeebe:completejob")
      }
    }

    val camel: CamelContext = DefaultCamelContext()


    camel.addComponent(ZeebeComponent.SCHEME, ZeebeComponent(Supplier { zeebe.client }))
    camel.addRoutes(routeBuilder)

    camel.start()

    startWorkflow()

    Awaitility.await().untilAsserted({
      records.find { it.metadata.intent == "COMPLETED" } != null
    })
  }

  private fun createTopic() =
      zeebe.client
          .newCreateTopicCommand()
          .name("topic")
          .partitions(1)
          .replicationFactor(1)
          .send()
          .join()
          .let {
            logger.info { "topic: ${it.state}" }
          }

  private fun deploy() = zeebe.client
      .topicClient()
      .workflowClient()
      .newDeployCommand()
      .addResourceFromClasspath("dummy.bpmn")
      .send()
      .join()
      .let {
        logger.info { "deployment: ${it.state}" }
      }

  private fun startWorkflow(): WorkflowInstanceEvent {
    val event = zeebe.client
        .topicClient().workflowClient()
        .newCreateInstanceCommand()
        .bpmnProcessId("process_dummy")
        .latestVersion()
        .payload("{\"foo\": \"" + UUID.randomUUID().toString() + "\"}")
        .send()
        .join()

    logger.info { "workflow: ${event.state}" }

    return event
  }

  fun complete(client: JobClient, job: JobEvent) {
    logger.info { "\n\n\n\n\n\n\nJob: $job    \n\n\n\n\n\n\n\n" }


    client.newCompleteCommand(job)
        .payload("{\"bar\": \"" + 42 + "\"}")
        .send().join()

  }

  private fun subscribe() = zeebe.client.topicClient()
      .newSubscription()
      .name("record-logger")
      .recordHandler({ record ->
        records += record
        logger.info { "\n\n\n\n\n\n Record-Logger: ${record.metadata.key}  ${record.metadata.valueType}   ${record.toJson()}       \n\n\n\n\n\n\n\n" }
      })
      .startAtHeadOfTopic()
      .forcedStart()
      .open();

}

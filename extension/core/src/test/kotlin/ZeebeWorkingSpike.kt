package io.zeebe.camel

import io.zeebe.camel.api.DeployGateway
import io.zeebe.camel.api.JobWorker
import io.zeebe.camel.api.StartProcessGateway
import io.zeebe.camel.api.command.CompleteJobCommand
import io.zeebe.camel.api.command.DeployCommand
import io.zeebe.camel.api.command.StartProcessCommand
import io.zeebe.client.api.record.Record
import io.zeebe.test.ZeebeTestRule
import mu.KLogging
import org.apache.camel.CamelContext
import org.apache.camel.builder.ProxyBuilder
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
import org.awaitility.Awaitility.await
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
  fun `start process and work on task`() {
    subscribeLogger()

    // create default context for testing
    val camel: CamelContext = DefaultCamelContext()

    // allow sending deploy commands to route
    val deployGateway = ProxyBuilder(camel).endpoint("direct:deploy").build(DeployGateway::class.java)
    // allow sending start commands to route
    val startProcessGateway = ProxyBuilder(camel).endpoint("direct:start").build(StartProcessGateway::class.java)

    // register zeebe main component
    camel.addComponent(ZeebeComponent.SCHEME, ZeebeComponent(Supplier { zeebe.client }))

    data class ReturnPayload(val bar:Int)

    class JobWorkerBean : JobWorker<ReturnPayload> {
      override fun apply(event: io.zeebe.camel.api.event.JobEvent): CompleteJobCommand<ReturnPayload> = CompleteJobCommand(event, ReturnPayload(42))
    }

    // register routes
    camel.addRoutes(object : RouteBuilder() {
      override fun configure() {
        from("direct:start").to("zeebe:start-process")
        from("direct:deploy").to("zeebe:deployment")

        from("zeebe:jobworker")
            .bean(JobWorkerBean::class.java)
            .to("zeebe:complete-job")
      }
    })

    // start the context
    camel.start()

    data class Payload(val foo: String = UUID.randomUUID().toString())

    // use proxy to send deploy command to direct:deploy
    deployGateway.send(DeployCommand.of("/dummy.bpmn"))

    // use proxy to send start command to direct:start
    startProcessGateway.send(StartProcessCommand("process_dummy", payload =  Payload()))

    // the completion is done via route zeebe:jobworker->direct:foo->zeebe:complete

    await().untilAsserted {

      logger.info { "records: $records" }
      records.find { it.metadata.intent == "COMPLETED" } != null
    }
  }


  private fun subscribeLogger() = zeebe.client.topicClient()
      .newSubscription()
      .name("record-logger")
      .recordHandler { record ->
        records += record
        logger.info {
          """
              Record-Logger: ${record.metadata.key} ${record.metadata.valueType}
                  ${record.toJson()}
                    """
        }
      }
      .startAtHeadOfTopic()
      .forcedStart()
      .open();

}

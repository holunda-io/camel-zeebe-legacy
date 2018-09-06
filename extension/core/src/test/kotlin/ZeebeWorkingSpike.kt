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

  lateinit var deployGateway: DeployGateway
  lateinit var startGateway : StartProcessGateway

  data class ReturnPayload(val bar:Int)
  data class StartPayload(val foo: String)

  class JobWorkerBean : JobWorker<ReturnPayload> {
    override fun apply(event: io.zeebe.camel.api.event.JobEvent): CompleteJobCommand<ReturnPayload> = CompleteJobCommand(event, ReturnPayload(42))
  }

  private fun initCamel() {

    // create default context for testing
    val camel: CamelContext = DefaultCamelContext()

    // allow sending deploy commands to route
    deployGateway = ProxyBuilder(camel).endpoint("direct:deploy").build(DeployGateway::class.java)
    // allow sending start commands to route
    startGateway = ProxyBuilder(camel).endpoint("direct:start").build(StartProcessGateway::class.java)

    // register zeebe main component
    camel.addComponent(ZeebeComponent.SCHEME, ZeebeComponent(Supplier { zeebe.client }))


    // register routes
    camel.addRoutes(object : RouteBuilder() {
      override fun configure() {
        from("direct:start").to("zeebe:process/start")
        from("direct:deploy").to("zeebe:process/deploy")

        from("zeebe:job/subscribe?jobType=doSomething")
            .bean(JobWorkerBean::class.java)
            .to("zeebe:job/complete")
      }
    })

    // start the context
    camel.start()
  }

  @Test
  fun `start process and work on task`() {
    initCamel()
    subscribeLogger()

    // use proxy to send deploy command to direct:deploy
    deployGateway.send(DeployCommand.of("/dummy.bpmn"))

    // use proxy to send start command to direct:start
    startGateway.send(StartProcessCommand("process_dummy", payload =  StartPayload(UUID.randomUUID().toString())))

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

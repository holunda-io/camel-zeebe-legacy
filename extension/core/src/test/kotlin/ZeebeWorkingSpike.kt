package io.zeebe.camel

import com.fasterxml.jackson.databind.ObjectMapper
import io.zeebe.camel.api.AddSubscriptionGateway
import io.zeebe.camel.api.DeployGateway
import io.zeebe.camel.api.JobWorker
import io.zeebe.camel.api.StartProcessGateway
import io.zeebe.camel.api.command.AddSubscriptionCommand
import io.zeebe.camel.api.command.CompleteJobCommand
import io.zeebe.camel.api.command.DeployCommand
import io.zeebe.camel.api.command.StartProcessCommand
import io.zeebe.client.api.record.Record
import io.zeebe.test.ZeebeTestRule
import org.apache.camel.CamelContext
import org.apache.camel.builder.ProxyBuilder
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
import org.awaitility.Awaitility.await
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.slf4j.LoggerFactory
import java.util.*
import java.util.function.Supplier

class ZeebeWorkingSpike {

  companion object {
    val logger = LoggerFactory.getLogger(ZeebeWorkingSpike::class.java)
  }

  @get:Rule
  val zeebe = ZeebeTestRule()

  private val records = mutableListOf<Record>()

  lateinit var deployGateway: DeployGateway
  lateinit var startGateway : StartProcessGateway
  lateinit var subscribeGateway: AddSubscriptionGateway

  data class ReturnPayload(val bar:Int)
  data class StartPayload(val foo: String)


  class CompleteJob {
    fun apply(payloadJson: String) : ReturnPayload = ReturnPayload((ObjectMapper().convertValue(payloadJson, StartPayload::class.java)).foo.length)
  }

  private fun initCamel() {

    // create default context for testing
    val camel: CamelContext = DefaultCamelContext()

    // allow sending deploy commands to route
    deployGateway = ProxyBuilder(camel).endpoint("direct:deploy").build(DeployGateway::class.java)
    // allow sending start commands to route
    startGateway = ProxyBuilder(camel).endpoint("direct:start").build(StartProcessGateway::class.java)
    subscribeGateway = ProxyBuilder(camel).endpoint("direct:subscribe").build(AddSubscriptionGateway::class.java)

    // register zeebe main component
    camel.addComponent(ZeebeComponent.SCHEME, ZeebeComponent(Supplier { zeebe.client }))


    // register routes
    camel.addRoutes(object : RouteBuilder() {
      override fun configure() {
        from("direct:start").routeId("start the process")
            .to("zeebe:process/start")
        from("direct:deploy").routeId("deploy the process")
            .to("zeebe:process/deploy")
        from("direct:subscribe").routeId("subscribe")
            .to("zeebe:job/addSubscription")

//        from("zeebe:job/subscribe?jobType=doSomething&workerName=dummyCompletor")
//            .routeId("subscribe")
//            .bean(CompleteJob::class.java)
//            .to("zeebe:job/complete")
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

    subscribeGateway.send(AddSubscriptionCommand("doSomething", "dynamic", to = "file:/Users/jangalinski/msg"))

    // use proxy to send start command to direct:start
    startGateway.send(StartProcessCommand("process_dummy", payload =  StartPayload(UUID.randomUUID().toString())))

    // the completion is done via route zeebe:jobworker->direct:foo->zeebe:complete

    await().untilAsserted {
      logger.info("records: {}",records)
      records.find { it.metadata.intent == "COMPLETED" } != null
    }
  }


  private fun subscribeLogger() = zeebe.client.topicClient()
      .newSubscription()
      .name("record-logger")
      .recordHandler { record ->
        records += record
        logger.info(
          """
              Record-Logger: ${record.metadata.key} ${record.metadata.valueType}
                  ${record.toJson()}
                    """
        )
      }
      .startAtHeadOfTopic()
      .forcedStart()
      .open();

}

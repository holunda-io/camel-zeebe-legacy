package io.zeebe.camel

import io.zeebe.camel.endpoint.SubscribeJobWorkerEndpoint
import io.zeebe.client.ZeebeClient
import org.apache.camel.impl.DefaultCamelContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.Answers
import org.mockito.Mockito

@Deprecated("seems like this is not needed anymore?")
class ZeebeComponentTest {

    private val camel = DefaultCamelContext()
    private val client = Mockito.mock(ZeebeClient::class.java, Answers.RETURNS_DEEP_STUBS)

    init {
        camel.addComponent(ZeebeComponent.SCHEME, ZeebeComponent(client))
    }

    @Test
    fun `create jobworker endpoint`() {

        val endpoint = camel.getEndpoint("zeebe:jobworker", SubscribeJobWorkerEndpoint::class.java)

    }

    @After
    fun tearDown() {
        camel.stop()
    }
}

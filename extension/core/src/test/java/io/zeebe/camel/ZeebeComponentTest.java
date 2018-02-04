package io.zeebe.camel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import io.zeebe.camel.handler.task.TaskEndpoint;
import io.zeebe.camel.handler.task.TaskUri;
import io.zeebe.camel.handler.universal.UniversalEventEndpoint;
import io.zeebe.camel.handler.universal.UniversalEventUri;
import io.zeebe.client.ZeebeClient;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ZeebeComponentTest
{

    private final ZeebeClient client = mock(ZeebeClient.class);
    private final ZeebeComponent component = new ZeebeComponent(client);
    private DefaultCamelContext camelContext;

    @Before
    public void setUp() throws Exception
    {
        camelContext = new DefaultCamelContext();
        camelContext.addComponent(ZeebeComponent.SCHEME, component);
    }

    @Test
    public void create_universalEventHandler() throws Exception
    {
        final String uri = UniversalEventUri.topic(ZeebeComponent.DEFAULT_TOPIC).name("test").get();
        UniversalEventEndpoint endpoint = (UniversalEventEndpoint) component.createEndpoint(uri);

        assertThat(endpoint).isNotNull();

        assertThat(endpoint.getTopic()).isEqualTo(ZeebeComponent.DEFAULT_TOPIC);
        assertThat(endpoint.getName()).isEqualTo("test");
    }


    @Test
    public void create_taskHandler() throws Exception
    {
        final String uri = TaskUri.topic(ZeebeComponent.DEFAULT_TOPIC).type("type").lockOwner("lockedby").get();
        TaskEndpoint endpoint = (TaskEndpoint) component.createEndpoint(uri);

        assertThat(endpoint).isNotNull();
        assertThat(endpoint.getTopic()).isEqualTo(ZeebeComponent.DEFAULT_TOPIC);
        assertThat(endpoint.getOwner()).isEqualTo("lockedby");
        assertThat(endpoint.getType()).isEqualTo("type");
    }



    @After
    public void tearDown() throws Exception
    {
        camelContext.stop();
    }
}

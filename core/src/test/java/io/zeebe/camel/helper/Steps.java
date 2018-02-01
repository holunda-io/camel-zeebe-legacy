package io.zeebe.camel.helper;

import io.zeebe.camel.ZeebeComponent;
import io.zeebe.client.event.WorkflowInstanceEvent;
import io.zeebe.test.ZeebeTestRule;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class Steps
{

    private final ZeebeTestRule zeebe;

    private final CamelContext context = new DefaultCamelContext();

    public Steps(final ZeebeTestRule zeebe)
    {

        this.zeebe = zeebe;
        this.context.addComponent(ZeebeComponent.SCHEME, new ZeebeComponent(zeebe.getClient()));
    }


    public void deploy() {
        zeebe.getClient().workflows().deploy(zeebe.getDefaultTopic())
             .addResourceFromClasspath("dummy.bpmn")
             .execute();
    }

    public void startProcess() {

        final WorkflowInstanceEvent workflowInstance = zeebe.getClient().workflows().create(zeebe.getDefaultTopic())
                                                             .bpmnProcessId("process_dummy")
                                                             .payload("{\"foo\":\"hello\"}")
                                                             .latestVersion()
                                                             .execute();

    }
    public ZeebeTestRule getZeebe()
    {
        return zeebe;
    }

    public CamelContext getContext()
    {
        return context;
    }
}

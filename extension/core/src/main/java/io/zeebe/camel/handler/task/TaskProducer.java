package io.zeebe.camel.handler.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

@Slf4j
public class TaskProducer extends DefaultProducer
{

    public TaskProducer(final TaskEndpoint endpoint)
    {
        super(endpoint);
    }

    @Override
    public void process(Exchange exchange) throws Exception
    {
        log.info("running TaskHandlerProducer!");
    }
}

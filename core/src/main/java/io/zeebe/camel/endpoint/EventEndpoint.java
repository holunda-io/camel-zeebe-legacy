package io.zeebe.camel.endpoint;

import org.apache.camel.*;
import org.apache.camel.impl.DefaultEndpoint;

public class EventEndpoint extends DefaultEndpoint
{
    @Override
    public Producer createProducer() throws Exception
    {
        return new Producer()
        {
            @Override
            public Exchange createExchange()
            {
                return null;
            }

            @Override
            public Exchange createExchange(ExchangePattern exchangePattern)
            {
                return null;
            }

            @Override
            public Exchange createExchange(Exchange exchange)
            {
                return null;
            }

            @Override
            public Endpoint getEndpoint()
            {
                return null;
            }

            @Override
            public boolean isSingleton()
            {
                return false;
            }

            @Override
            public void process(Exchange exchange) throws Exception
            {

            }

            @Override
            public void start() throws Exception
            {

            }

            @Override
            public void stop() throws Exception
            {

            }
        };
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception
    {
        return null;
    }

    @Override
    public boolean isSingleton()
    {
        return false;
    }
}

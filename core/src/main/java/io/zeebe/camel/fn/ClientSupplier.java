package io.zeebe.camel.fn;

import io.zeebe.client.ZeebeClient;

public interface ClientSupplier
{
    ZeebeClient getClient();
}

package io.zeebe.camel;

import io.zeebe.client.ZeebeClient;

public interface ClientSupplier
{
    ZeebeClient getClient();
}

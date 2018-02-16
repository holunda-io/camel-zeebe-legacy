package io.zeebe.camel.fn;

import io.zeebe.client.impl.ZeebeClientImpl;

public interface ClientSupplier
{
    ZeebeClientImpl getClient();
}

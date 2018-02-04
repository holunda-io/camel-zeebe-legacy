package io.zeebe.camel.handler.universal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class UniversalEventUriTest
{
    @Test
    public void create_without_parameters()
    {
        assertThat(UniversalEventUri.topic("the-topic").get()).startsWith("zeebe://the-topic?name=");
    }

    @Test
    public void create_with_name()
    {
        assertThat(UniversalEventUri.topic("the-topic").name("the-name").get()).isEqualTo("zeebe://the-topic?name=the-name");
    }
}

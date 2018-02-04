package io.zeebe.camel.fn;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CamelUriTest
{
    @Test
    public void with_parameters() {
        String uri = CamelUri.builder()
                             .scheme("muppetshow")
                             .base("local")
                             .base("so")
                             .base("what/ever")
                             .parameter("foo", "bar")
                             .parameter("zee", "loo")
                             .build()
                             .toString();
        assertThat(uri).isEqualTo("muppetshow://local/so/what/ever?foo=bar&zee=loo");
    }

    @Test
    public void without_parameters() {
        String uri = CamelUri.builder()
                             .scheme("muppetshow")
                             .base("local/so/what/ever")
                             .build()
                             .toString();
        assertThat(uri).isEqualTo("muppetshow://local/so/what/ever");
    }
}

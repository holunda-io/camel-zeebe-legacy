package io.zeebe.camel.fn;

import io.zeebe.camel.fn.CamelUriBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CamelUriBuilderTest {
    @Test
    public void testBuild() {
        String uri = CamelUriBuilder.builder()
                                    .scheme("muppetshow")
                                    .base("local/so/what/ever")
                                    .parameter("foo", "bar")
                                    .parameter("zee", "loo")
                                    .build()
                                    .toString();
        assertThat(uri).isEqualTo("muppetshow://local/so/what/ever?foo=bar&zee=loo");
    }
}

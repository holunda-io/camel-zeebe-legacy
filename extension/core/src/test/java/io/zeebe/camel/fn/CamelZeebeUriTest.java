package io.zeebe.camel.fn;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CamelZeebeUriTest {

    static class TestUri extends CamelZeebeUri<TestUri> {

        public TestUri(final String base, final String foo, final String zee) {
            super(base);
            parameter("foo", foo);
            parameter("zee", zee);
        }
    }

    @Test
    public void testUri_toString() {
        assertThat(new TestUri("base", "bar", "be").get()).isEqualTo("zeebe://base?foo=bar&zee=be");
    }
}

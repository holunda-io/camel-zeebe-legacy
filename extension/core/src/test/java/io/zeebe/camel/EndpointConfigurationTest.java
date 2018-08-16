package io.zeebe.camel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import io.zeebe.camel.fn.CamelZeebeUri;
import io.zeebe.camel.handler.Handler;
import org.junit.Test;

public class EndpointConfigurationTest {

    @Test(expected = IllegalArgumentException.class)
    public void remaining_fails_for_null() {
        EndpointConfiguration.remaining(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void remaining_fails_for_blank() {
        EndpointConfiguration.remaining("      \n   ");
    }

    @Test
    public void remaining_only_topic() {
        EndpointConfiguration.Remaining remaining = EndpointConfiguration.remaining("topic");

        assertThat(remaining.getTopic()).isEqualTo("topic");
        assertThat(remaining.getSubject()).isEmpty();
        assertThat(remaining.getType()).isEmpty();
    }

    @Test
    public void remaining_topic_and_subject() {
        EndpointConfiguration.Remaining remaining = EndpointConfiguration.remaining("topic/job");

        assertThat(remaining.getTopic()).isEqualTo("topic");
        assertThat(remaining.getSubject()).hasValue("job");
        assertThat(remaining.getType()).isEmpty();
    }

    @Test
    public void remaining_topic_and_subject_and_operation() {
        EndpointConfiguration.Remaining remaining = EndpointConfiguration
            .remaining("topic/job/type");

        assertThat(remaining.getTopic()).isEqualTo("topic");
        assertThat(remaining.getSubject()).hasValue("job");
        assertThat(remaining.getType()).hasValue("type");
    }

    @Test
    public void get_universal_handler() {
        EndpointConfiguration configuration = configuration(new DummyUri("topic"));

        // FIXME  assertThat(configuration.getHandler()).isEqualTo(Handler.UNIVERSAL_EVENT);
    }

    private static EndpointConfiguration configuration(DummyUri dummyUri) {
        return new EndpointConfiguration(dummyUri.get(), dummyUri.getBase(),
            dummyUri.getParameters(), mock(ZeebeComponent.class));
    }


    private static class DummyUri extends CamelZeebeUri<DummyUri> {

        public DummyUri(String topic) {
            super(topic);
        }

        @Override
        public DummyUri base(String base) {
            return super.base(base);
        }

        @Override
        public DummyUri parameter(String key, Object value) {
            return super.parameter(key, value);
        }
    }
}

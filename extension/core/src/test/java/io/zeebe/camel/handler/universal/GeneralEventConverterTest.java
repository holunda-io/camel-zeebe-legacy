package io.zeebe.camel.handler.universal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.zeebe.camel.api.event.GeneralEvent;
import io.zeebe.client.event.impl.GeneralEventImpl;

public class GeneralEventConverterTest {

    @Test
    public void convert_back_and_forth() {
        final GeneralEvent event = GeneralEvent.builder()
            .build();

        final GeneralEventImpl impl = GeneralEventConverter.convert(event);

        assertThat(GeneralEventConverter.convert(impl)).isEqualTo(event);
    }
}

package io.zeebe.camel.test.json;

import java.util.function.Supplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.zeebe.client.impl.data.MsgPackConverter;
import org.msgpack.jackson.dataformat.MessagePackFactory;

public class ZeebeObjectMapper
{

    public static ObjectMapper INSTANCE = ((Supplier<ObjectMapper>) () -> {
        final MsgPackConverter msgPackConverter = new MsgPackConverter();
        final MessagePackFactory messagePackFactory = new MessagePackFactory().setReuseResourceInGenerator(false).setReuseResourceInParser(false);
        final ObjectMapper objectMapper = new ObjectMapper(messagePackFactory);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        final InjectableValues injectionContext = new InjectableValues.Std().addValue(MsgPackConverter.class, msgPackConverter);
        objectMapper.setInjectableValues(injectionContext);

        return objectMapper;
    }).get();

}

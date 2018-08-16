package io.zeebe.camel.api.record;

import java.util.Map;
import lombok.Value;

@Value
public class RecordHeader {
    private Map<String, Object> value;
}

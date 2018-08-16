package io.zeebe.camel.handler;

import io.zeebe.camel.EndpointConfiguration;
import io.zeebe.camel.ZeebeEndpoint;
import io.zeebe.camel.handler.task.TaskEndpoint;
import io.zeebe.camel.handler.universal.UniversalEventEndpoint;
import io.zeebe.client.api.subscription.JobHandler;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

/**
 * Each supported handler is listed here.
 */
@Getter
public enum Handler {
    TASK(TaskEndpoint.SUBJECT, JobHandler.class) {
        @Override
        public TaskEndpoint createEndpoint(final EndpointConfiguration configuration) {
            return new TaskEndpoint(configuration);
        }
    },
    // FIXME: implement
//    UNIVERSAL_EVENT(UniversalEventEndpoint.SUBJECT, Glo.class) {
//        @Override
//        public UniversalEventEndpoint createEndpoint(final EndpointConfiguration configuration) {
//            return new UniversalEventEndpoint(configuration);
//        }
//    },
    //
    ;

    public static final Map<String, Handler> BY_SUBJECT = Stream.of(Handler.values())
        .collect(Collectors.toMap(Handler::getSubject, e -> e));

    private final String subject;
    private final Class<?> zeebeHandler;

    Handler(String subject, Class<?> zeebeHandler) {
        this.subject = subject;

        this.zeebeHandler = zeebeHandler;
    }

    public abstract ZeebeEndpoint createEndpoint(final EndpointConfiguration configuration);
}

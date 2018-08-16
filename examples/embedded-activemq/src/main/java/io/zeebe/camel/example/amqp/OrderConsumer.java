package io.zeebe.camel.example.amqp;

import io.zeebe.camel.example.amqp.model.Order;
import javax.jms.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderConsumer {

    @JmsListener(destination = ActiveMQConfig.ORDER_QUEUE)
    public void receiveMessage(@Payload Order order,
        @Headers MessageHeaders headers,
        Message message, Session session) {

        log.info("- - - - - - - - - - - - - - - - - - - - - - - -\n"
            + "###### received <{}> #####\n"
            + "- - - - - - - - - - - - - - - - - - - - - - - -\n"
            + "headers: {}\n"
            + "message: {}\n"
            + "session: {}\n", order, headers, message, session);
    }

}

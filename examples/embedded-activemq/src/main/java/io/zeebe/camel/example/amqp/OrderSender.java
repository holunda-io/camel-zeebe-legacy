package io.zeebe.camel.example.amqp;

import io.zeebe.camel.example.amqp.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(Order myMessage) {
        log.info("sending with convertAndSend() to queue <" + myMessage + ">");
        jmsTemplate.convertAndSend(ActiveMQConfig.ORDER_QUEUE, myMessage);
    }
}

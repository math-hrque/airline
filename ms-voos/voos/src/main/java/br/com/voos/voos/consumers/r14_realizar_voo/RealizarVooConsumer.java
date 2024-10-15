package br.com.voos.voos.consumers.r14_realizar_voo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RealizarVooConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

}

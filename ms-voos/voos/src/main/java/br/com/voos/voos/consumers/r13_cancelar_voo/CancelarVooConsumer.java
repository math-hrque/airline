package br.com.voos.voos.consumers.r13_cancelar_voo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CancelarVooConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

}

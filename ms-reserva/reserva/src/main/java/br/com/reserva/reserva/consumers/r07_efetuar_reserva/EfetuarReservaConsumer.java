package br.com.reserva.reserva.consumers.r07_efetuar_reserva;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EfetuarReservaConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

}

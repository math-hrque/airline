package br.com.reserva.reserva.consumers.r08_cancelar_reserva;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CancelarReservaConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

}

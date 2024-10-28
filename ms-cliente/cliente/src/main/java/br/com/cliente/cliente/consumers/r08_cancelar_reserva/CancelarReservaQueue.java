package br.com.cliente.cliente.consumers.r08_cancelar_reserva;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class CancelarReservaQueue {

    @Bean
    public Queue milhasReservaCancelarQueue() {
        return new Queue("ms-cliente-milhas-reserva-cancelar");
    }

    @Bean
    public Queue milhasReservaCanceladaCompensarQueue() {
        return new Queue("ms-cliente-milhas-reserva-cancelada-compensar");
    }

    @Bean
    public Binding milhasReservaCancelarBinding(Queue milhasReservaCancelarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(milhasReservaCancelarQueue).to(exchange).with("ms-cliente-milhas-reserva-cancelar");
    }

    @Bean
    public Binding milhasReservaCanceladaCompensarBinding(Queue milhasReservaCanceladaCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(milhasReservaCanceladaCompensarQueue).to(exchange).with("ms-cliente-milhas-reserva-cancelada-compensar");
    }
}

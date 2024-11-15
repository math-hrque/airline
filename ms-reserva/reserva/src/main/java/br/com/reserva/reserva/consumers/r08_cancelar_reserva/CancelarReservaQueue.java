package br.com.reserva.reserva.consumers.r08_cancelar_reserva;

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
    public Queue reservaCancelarQueue() {
        return new Queue("ms-reserva-reserva-cancelar");
    }

    @Bean
    public Queue reservaCanceladaCompensarQueue() {
        return new Queue("ms-reserva-reserva-cancelada-compensar");
    }

    @Bean
    public Queue reservaCanceladaContaRQueue() {
        return new Queue("ms-reserva-reserva-cancelada-contaR");
    }

    @Bean
    public Queue reservaCanceladaContaRCompensarQueue() {
        return new Queue("ms-reserva-reserva-cancelada-contaR-compensar");
    }

    @Bean
    public Binding reservaCancelarBinding(Queue reservaCancelarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaCancelarQueue).to(exchange).with("ms-reserva-reserva-cancelar");
    }

    @Bean
    public Binding reservaCanceladaCompensarBinding(Queue reservaCanceladaCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaCanceladaCompensarQueue).to(exchange).with("ms-reserva-reserva-cancelada-compensar");
    }

    @Bean
    public Binding reservaCanceladaContaRBinding(Queue reservaCanceladaContaRQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaCanceladaContaRQueue).to(exchange).with("ms-reserva-reserva-cancelada-contaR");
    }

    @Bean
    public Binding reservaCanceladaContaRCompensarBinding(Queue reservaCanceladaContaRCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaCanceladaContaRCompensarQueue).to(exchange).with("ms-reserva-reserva-cancelada-contaR-compensar");
    }
}

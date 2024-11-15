package br.com.reserva.reserva.consumers.r13_cancelar_voo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class CancelarReservasVooQueue {

    @Bean
    public Queue reservasCancelarVooQueue() {
        return new Queue("ms-reserva-reservas-cancelar-voo");
    }

    @Bean
    public Queue reservasCanceladasVooCompensarQueue() {
        return new Queue("ms-reserva-reservas-canceladas-voo-compensar");
    }

    @Bean
    public Queue reservasCanceladasVooContaRQueue() {
        return new Queue("ms-reserva-reservas-canceladas-voo-contaR");
    }

    @Bean
    public Queue reservasCanceladasVooContaRCompensarQueue() {
        return new Queue("ms-reserva-reservas-canceladas-voo-contaR-compensar");
    }

    @Bean
    public Binding reservasCancelarVooBinding(Queue reservasCancelarVooQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservasCancelarVooQueue).to(exchange).with("ms-reserva-reservas-cancelar-voo");
    }

    @Bean
    public Binding reservasCanceladasVooCompensarBinding(Queue reservasCanceladasVooCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservasCanceladasVooCompensarQueue).to(exchange).with("ms-reserva-reservas-canceladas-voo-compensar");
    }

    @Bean
    public Binding reservasCanceladasVooContaRBinding(Queue reservasCanceladasVooContaRQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservasCanceladasVooContaRQueue).to(exchange).with("ms-reserva-reservas-canceladas-voo-contaR");
    }

    @Bean
    public Binding reservasCanceladasVooContaRCompensarBinding(Queue reservasCanceladasVooContaRCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservasCanceladasVooContaRCompensarQueue).to(exchange).with("ms-reserva-reservas-canceladas-voo-contaR-compensar");
    }
}

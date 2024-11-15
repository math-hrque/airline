package br.com.reserva.reserva.consumers.r14_realizar_voo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class RealizarReservasVooQueue {

    @Bean
    public Queue reservaRealizarQueue() {
        return new Queue("ms-reserva-reservas-realizar-voo");
    }

    @Bean
    public Queue reservasRealizadasCompensarQueue() {
        return new Queue("ms-reserva-reservas-realizadas-voo-compensar");
    }

    @Bean
    public Queue reservasRealizadasContaRQueue() {
        return new Queue("ms-reserva-reservas-realizadas-voo-contaR");
    }

    @Bean
    public Queue reservasRealizadasContaRCompensarQueue() {
        return new Queue("ms-reserva-reservas-realizadas-voo-contaR-compensar");
    }

    @Bean
    public Binding reservaRealizarBinding(Queue reservaRealizarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaRealizarQueue).to(exchange).with("ms-reserva-reservas-realizar-voo");
    }

    @Bean
    public Binding reservasRealizadasCompensarBinding(Queue reservasRealizadasCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservasRealizadasCompensarQueue).to(exchange).with("ms-reserva-reservas-realizadas-voo-compensar");
    }

    @Bean
    public Binding reservasRealizadasContaRBinding(Queue reservasRealizadasContaRQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservasRealizadasContaRQueue).to(exchange).with("ms-reserva-reservas-realizadas-voo-contaR");
    }

    @Bean
    public Binding reservasRealizadasContaRCompensarBinding(Queue reservasRealizadasContaRCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservasRealizadasContaRCompensarQueue).to(exchange).with("ms-reserva-reservas-realizadas-voo-contaR-compensar");
    }
}

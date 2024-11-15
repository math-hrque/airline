package br.com.reserva.reserva.consumers.r07_efetuar_reserva;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class EfetuarReservaQueue {

    @Bean
    public Queue reservaCadastrarQueue() {
        return new Queue("ms-reserva-reserva-cadastrar");
    }

    @Bean
    public Queue reservaCadastradaCompensarQueue() {
        return new Queue("ms-reserva-reserva-cadastrada-compensar");
    }

    @Bean
    public Queue reservaCadastradaContaRQueue() {
        return new Queue("ms-reserva-reserva-cadastrada-contaR");
    }

    @Bean
    public Queue reservaCadastradaContaRCompensarQueue() {
        return new Queue("ms-reserva-reserva-cadastrada-contaR-compensar");
    }

    @Bean
    public Binding reservaCadastrarBinding(Queue reservaCadastrarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaCadastrarQueue).to(exchange).with("ms-reserva-reserva-cadastrar");
    }

    @Bean
    public Binding reservaCadastradaCompensarBinding(Queue reservaCadastradaCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaCadastradaCompensarQueue).to(exchange).with("ms-reserva-reserva-cadastrada-compensar");
    }

    @Bean
    public Binding reservaCadastradaContaRBinding(Queue reservaCadastradaContaRQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaCadastradaContaRQueue).to(exchange).with("ms-reserva-reserva-cadastrada-contaR");
    }

    @Bean
    public Binding reservaCadastradaContaRCompensarBinding(Queue reservaCadastradaContaRCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaCadastradaContaRCompensarQueue).to(exchange).with("ms-reserva-reserva-cadastrada-contaR-compensar");
    }
}

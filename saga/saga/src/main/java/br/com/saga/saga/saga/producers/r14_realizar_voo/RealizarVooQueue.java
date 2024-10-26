package br.com.saga.saga.saga.producers.r14_realizar_voo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class RealizarVooQueue {

    @Bean
    public Queue vooRealizadoQueue() {
        return new Queue("saga-ms-voos-voo-realizado");
    }

    @Bean
    public Queue vooRealizadoErroQueue() {
        return new Queue("saga-ms-voos-voo-realizado-erro");
    }

    @Bean
    public Queue reservasRealizadasQueue() {
        return new Queue("saga-ms-reserva-reservas-realizadas");
    }

    @Bean
    public Queue reservasRealizadasErroQueue() {
        return new Queue("saga-ms-reserva-reservas-realizadas-erro");
    }

    @Bean
    public Binding vooRealizadoBinding(Queue vooRealizadoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooRealizadoQueue).to(exchange).with("saga-ms-voos-voo-realizado");
    }

    @Bean
    public Binding vooRealizadoErroBinding(Queue vooRealizadoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooRealizadoErroQueue).to(exchange).with("saga-ms-voos-voo-realizado-erro");
    }

    @Bean
    public Binding reservasRealizadasBinding(Queue reservasRealizadasQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservasRealizadasQueue).to(exchange).with("saga-ms-reserva-reservas-realizadas");
    }

    @Bean
    public Binding reservasRealizadasErroBinding(Queue reservasRealizadasErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservasRealizadasErroQueue).to(exchange).with("saga-ms-reserva-reservas-realizadas-erro");
    }
}

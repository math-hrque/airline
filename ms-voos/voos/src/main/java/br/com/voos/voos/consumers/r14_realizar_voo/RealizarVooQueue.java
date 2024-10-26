package br.com.voos.voos.consumers.r14_realizar_voo;

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
    public Queue vooRealizarQueue() {
        return new Queue("ms-voos-voo-realizar");
    }

    @Bean
    public Queue vooRealizadoCompensarQueue() {
        return new Queue("ms-voos-voo-realizado-compensar");
    }

    @Bean
    public Binding vooRealizarBinding(Queue vooRealizarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooRealizarQueue).to(exchange).with("ms-voos-voo-realizar");
    }

    @Bean
    public Binding vooRealizadoCompensarBinding(Queue vooRealizadoCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooRealizadoCompensarQueue).to(exchange).with("ms-voos-voo-realizado-compensar");
    }
}

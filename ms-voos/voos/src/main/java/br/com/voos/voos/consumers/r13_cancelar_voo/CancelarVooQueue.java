package br.com.voos.voos.consumers.r13_cancelar_voo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class CancelarVooQueue {

    @Bean
    public Queue vooCancelarQueue() {
        return new Queue("ms-voos-voo-cancelar");
    }

    @Bean
    public Queue vooCanceladoCompensarQueue() {
        return new Queue("ms-voos-voo-cancelado-compensar");
    }

    @Bean
    public Binding vooCancelarBinding(Queue vooCancelarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooCancelarQueue).to(exchange).with("ms-voos-voo-cancelar");
    }

    @Bean
    public Binding vooCanceladoCompensarBinding(Queue vooCanceladoCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooCanceladoCompensarQueue).to(exchange).with("ms-voos-voo-cancelado-compensar");
    }
}

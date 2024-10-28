package br.com.reserva.reserva.consumers.r12_confirmar_embarque;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ConfirmarEmbarqueQueue {

    @Bean
    public Queue embarqueConfirmarQueue() {
        return new Queue("ms-reserva-reserva-confirmar-embarque-contaR");
    }

    @Bean
    public Binding embarqueConfirmarBinding(Queue embarqueConfirmarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(embarqueConfirmarQueue).to(exchange).with("ms-reserva-reserva-confirmar-embarque-contaR");
    }
}

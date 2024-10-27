package br.com.reserva.reserva.consumers.r10_fazer_checkin;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class FazerCheckinQueue {

    @Bean
    public Queue checkinFazerQueue() {
        return new Queue("ms-reserva-fazer-checkin-contaR");
    }

    @Bean
    public Binding checkinFazerBinding(Queue checkinFazerQueue, TopicExchange exchange) {
        return BindingBuilder.bind(checkinFazerQueue).to(exchange).with("ms-reserva-fazer-checkin-contaR");
    }
}

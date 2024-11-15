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
    public Queue checkinFazerContaRQueue() {
        return new Queue("ms-reserva-reserva-fazer-checkin-contaR");
    }

    @Bean
    public Queue checkinFeitoContaRCompensarQueue() {
        return new Queue("ms-reserva-reserva-feito-checkin-contaR-compensar");
    }

    @Bean
    public Queue checkinFeitoContaCUDCompensarQueue() {
        return new Queue("ms-reserva-reserva-feito-checkin-contaCUD-compensar");
    }

    @Bean
    public Binding checkinFazerContaRBinding(Queue checkinFazerContaRQueue, TopicExchange exchange) {
        return BindingBuilder.bind(checkinFazerContaRQueue).to(exchange).with("ms-reserva-reserva-fazer-checkin-contaR");
    }

    @Bean
    public Binding checkinFeitoContaRCompensarBinding(Queue checkinFeitoContaRCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(checkinFeitoContaRCompensarQueue).to(exchange).with("ms-reserva-reserva-feito-checkin-contaR-compensar");
    }

    @Bean
    public Binding checkinFeitoContaCUDCompensarBinding(Queue checkinFeitoContaCUDCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(checkinFeitoContaCUDCompensarQueue).to(exchange).with("ms-reserva-reserva-feito-checkin-contaCUD-compensar");
    }
}

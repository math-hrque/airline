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
    public Queue embarqueConfirmarContaRQueue() {
        return new Queue("ms-reserva-reserva-confirmar-embarque-contaR");
    }

    @Bean
    public Queue embarqueConfirmadoContaRCompensarQueue() {
        return new Queue("ms-reserva-reserva-confirmado-embarque-contaR-compensar");
    }

    @Bean
    public Queue embarqueConfirmadoContaCUDCompensarQueue() {
        return new Queue("ms-reserva-reserva-confirmado-embarque-contaCUD-compensar");
    }

    @Bean
    public Binding embarqueConfirmarContaRBinding(Queue embarqueConfirmarContaRQueue, TopicExchange exchange) {
        return BindingBuilder.bind(embarqueConfirmarContaRQueue).to(exchange).with("ms-reserva-reserva-confirmar-embarque-contaR");
    }

    @Bean
    public Binding embarqueConfirmadoContaRCompensarBinding(Queue embarqueConfirmadoContaRCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(embarqueConfirmadoContaRCompensarQueue).to(exchange).with("ms-reserva-reserva-confirmado-embarque-contaR-compensar");
    }

    @Bean
    public Binding embarqueConfirmadoContaCUDCompensarBinding(Queue embarqueConfirmadoContaCUDCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(embarqueConfirmadoContaCUDCompensarQueue).to(exchange).with("ms-reserva-reserva-confirmado-embarque-contaCUD-compensar");
    }
}

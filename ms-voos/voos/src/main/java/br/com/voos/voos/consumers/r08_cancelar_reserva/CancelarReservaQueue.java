package br.com.voos.voos.consumers.r08_cancelar_reserva;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class CancelarReservaQueue {

    @Bean
    public Queue vooPoltronaDesocuparQueue() {
        return new Queue("ms-voos-voo-poltrona-desocupar");
    }

    @Bean
    public Queue vooPoltronaDesocupadaCompensarQueue() {
        return new Queue("ms-voos-voo-poltrona-desocupada-compensar");
    }

    @Bean
    public Binding vooPoltronaDesocupadaBinding(Queue vooPoltronaDesocuparQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooPoltronaDesocuparQueue).to(exchange).with("ms-voos-voo-poltrona-desocupar");
    }

    @Bean
    public Binding vooPoltronaDesocupadaCompensarBinding(Queue vooPoltronaDesocupadaCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooPoltronaDesocupadaCompensarQueue).to(exchange).with("ms-voos-voo-poltrona-desocupada-compensar");
    }
}

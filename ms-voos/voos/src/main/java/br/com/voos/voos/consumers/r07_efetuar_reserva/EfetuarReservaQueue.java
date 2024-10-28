package br.com.voos.voos.consumers.r07_efetuar_reserva;

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
    public Queue vooPoltronaOcuparQueue() {
        return new Queue("ms-voos-voo-poltrona-ocupar");
    }

    @Bean
    public Queue vooPoltronaOcupadaCompensarQueue() {
        return new Queue("ms-voos-voo-poltrona-ocupada-compensar");
    }

    @Bean
    public Binding vooPoltronaOcuparBinding(Queue vooPoltronaOcuparQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooPoltronaOcuparQueue).to(exchange).with("ms-voos-voo-poltrona-ocupar");
    }

    @Bean
    public Binding vooPoltronaOcupadaCompensarBinding(Queue vooPoltronaOcupadaCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooPoltronaOcupadaCompensarQueue).to(exchange).with("ms-voos-voo-poltrona-ocupada-compensar");
    }
}

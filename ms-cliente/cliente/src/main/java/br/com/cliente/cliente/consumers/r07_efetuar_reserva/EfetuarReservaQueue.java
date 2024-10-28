package br.com.cliente.cliente.consumers.r07_efetuar_reserva;

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
    public Queue milhasReservaCadastrarQueue() {
        return new Queue("ms-cliente-milhas-reserva-cadastrar");
    }

    @Bean
    public Queue milhasReservaCadastradaCompensarQueue() {
        return new Queue("ms-cliente-milhas-reserva-cadastrada-compensar");
    }

    @Bean
    public Binding milhasReservaCadastrarBinding(Queue milhasReservaCadastrarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(milhasReservaCadastrarQueue).to(exchange).with("ms-cliente-milhas-reserva-cadastrar");
    }

    @Bean
    public Binding milhasReservaCadastradaCompensarBinding(Queue milhasReservaCadastradaCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(milhasReservaCadastradaCompensarQueue).to(exchange).with("ms-cliente-milhas-reserva-cadastrada-compensar");
    }
}

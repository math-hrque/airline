package br.com.saga.saga.saga.producers.r13_cancelar_voo;

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
    public Queue vooCanceladoQueue() {
        return new Queue("saga-ms-voos-voo-cancelado");
    }

    @Bean
    public Queue vooCanceladoErroQueue() {
        return new Queue("saga-ms-voos-voo-cancelado-erro");
    }

    @Bean
    public Queue reservasCanceladasVooQueue() {
        return new Queue("saga-ms-reserva-reservas-canceladas-voo");
    }

    @Bean
    public Queue reservasCanceladasVooErroQueue() {
        return new Queue("saga-ms-reserva-reservas-canceladas-voo-erro");
    }

    @Bean
    public Queue milhasReservasCanceladasVooQueue() {
        return new Queue("saga-ms-cliente-milhas-reservas-canceladas-voo");
    }

    @Bean
    public Queue milhasReservasCanceladasVooErroQueue() {
        return new Queue("saga-ms-cliente-milhas-reservas-canceladas-voo-erro");
    }

    @Bean
    public Binding vooCanceladoBinding(Queue vooCanceladoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooCanceladoQueue).to(exchange).with("saga-ms-voos-voo-cancelado");
    }

    @Bean
    public Binding vooCanceladoErroBinding(Queue vooCanceladoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooCanceladoErroQueue).to(exchange).with("saga-ms-voos-voo-cancelado-erro");
    }

    @Bean
    public Binding reservasCanceladasVooBinding(Queue reservasCanceladasVooQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservasCanceladasVooQueue).to(exchange).with("saga-ms-reserva-reservas-canceladas-voo");
    }

    @Bean
    public Binding reservasCanceladasVooErroBinding(Queue reservasCanceladasVooErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservasCanceladasVooErroQueue).to(exchange).with("saga-ms-reserva-reservas-canceladas-voo-erro");
    }
 
    @Bean
    public Binding milhasReservasCanceladasVooBinding(Queue milhasReservasCanceladasVooQueue, TopicExchange exchange) {
        return BindingBuilder.bind(milhasReservasCanceladasVooQueue).to(exchange).with("saga-ms-cliente-milhas-reservas-canceladas-voo");
    }

    @Bean
    public Binding milhasReservasCanceladasVooErroBinding(Queue milhasReservasCanceladasVooErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(milhasReservasCanceladasVooErroQueue).to(exchange).with("saga-ms-cliente-milhas-reservas-canceladas-voo-erro");
    }
}

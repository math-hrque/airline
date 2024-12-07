package br.com.saga.saga.saga.producers.r07_efetuar_reserva;

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
    public Queue reservaCadastradaQueue() {
        return new Queue("saga-ms-reserva-reserva-cadastrada");
    }

    @Bean
    public Queue reservaCadastradaEndpointQueue() {
        return new Queue("saga-ms-reserva-reserva-cadastrada-endpoint");
    }

    @Bean
    public Queue reservaCadastradaErroQueue() {
        return new Queue("saga-ms-reserva-reserva-cadastrada-erro");
    }

    @Bean
    public Queue vooPoltronaOcupadaQueue() {
        return new Queue("saga-ms-voos-voo-poltrona-ocupada");
    }

    @Bean
    public Queue vooPoltronaOcupadaErroQueue() {
        return new Queue("saga-ms-voos-voo-poltrona-ocupada-erro");
    }

    @Bean
    public Queue milhasReservaCadastradaQueue() {
        return new Queue("saga-ms-cliente-milhas-reserva-cadastrada");
    }

    @Bean
    public Queue milhasReservaCadastradaErroQueue() {
        return new Queue("saga-ms-cliente-milhas-reserva-cadastrada-erro");
    }

    @Bean
    public Binding reservaCadastradaBinding(Queue reservaCadastradaQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaCadastradaQueue).to(exchange).with("saga-ms-reserva-reserva-cadastrada");
    }

    @Bean
    public Binding reservaCadastradaEndpointBinding(Queue reservaCadastradaEndpointQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaCadastradaEndpointQueue).to(exchange).with("saga-ms-reserva-reserva-cadastrada-endpoint");
    }

    @Bean
    public Binding reservaCadastradaErroBinding(Queue reservaCadastradaErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaCadastradaErroQueue).to(exchange).with("saga-ms-reserva-reserva-cadastrada-erro");
    }

    @Bean
    public Binding vooPoltronaOcupadaBinding(Queue vooPoltronaOcupadaQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooPoltronaOcupadaQueue).to(exchange).with("saga-ms-voos-voo-poltrona-ocupada");
    }

    @Bean
    public Binding vooPoltronaOcupadaErroBinding(Queue vooPoltronaOcupadaErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooPoltronaOcupadaErroQueue).to(exchange).with("saga-ms-voos-voo-poltrona-ocupada-erro");
    }
 
    @Bean
    public Binding milhasReservaCadastradaBinding(Queue milhasReservaCadastradaQueue, TopicExchange exchange) {
        return BindingBuilder.bind(milhasReservaCadastradaQueue).to(exchange).with("saga-ms-cliente-milhas-reserva-cadastrada");
    }

    @Bean
    public Binding milhasReservaCadastradaErroBinding(Queue milhasReservaCadastradaErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(milhasReservaCadastradaErroQueue).to(exchange).with("saga-ms-cliente-milhas-reserva-cadastrada-erro");
    }
}

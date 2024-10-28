package br.com.saga.saga.saga.producers.r08_cancelar_reserva;

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
    public Queue reservaCanceladaQueue() {
        return new Queue("saga-ms-reserva-reserva-cancelada");
    }

    @Bean
    public Queue reservaCanceladaErroQueue() {
        return new Queue("saga-ms-reserva-reserva-cancelada-erro");
    }

    @Bean
    public Queue vooPoltronaDesocupadaQueue() {
        return new Queue("saga-ms-voos-voo-poltrona-desocupada");
    }

    @Bean
    public Queue vooPoltronaDesocupadaErroQueue() {
        return new Queue("saga-ms-voos-voo-poltrona-desocupada-erro");
    }

    @Bean
    public Queue milhasReservaCanceladaQueue() {
        return new Queue("saga-ms-cliente-milhas-reserva-cancelada");
    }

    @Bean
    public Queue milhasReservaCanceladaErroQueue() {
        return new Queue("saga-ms-cliente-milhas-reserva-cancelada-erro");
    }

    @Bean
    public Binding reservaCanceladaBinding(Queue reservaCanceladaQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaCanceladaQueue).to(exchange).with("saga-ms-reserva-reserva-cancelada");
    }

    @Bean
    public Binding reservaCanceladaErroBinding(Queue reservaCanceladaErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(reservaCanceladaErroQueue).to(exchange).with("saga-ms-reserva-reserva-cancelada-erro");
    }

    @Bean
    public Binding vooPoltronaDesocupadaBinding(Queue vooPoltronaDesocupadaQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooPoltronaDesocupadaQueue).to(exchange).with("saga-ms-voos-voo-poltrona-desocupada");
    }

    @Bean
    public Binding vooPoltronaDesocupadaErroBinding(Queue vooPoltronaDesocupadaErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(vooPoltronaDesocupadaErroQueue).to(exchange).with("saga-ms-voos-voo-poltrona-desocupada-erro");
    }
 
    @Bean
    public Binding milhasReservaCanceladaBinding(Queue milhasReservaCanceladaQueue, TopicExchange exchange) {
        return BindingBuilder.bind(milhasReservaCanceladaQueue).to(exchange).with("saga-ms-cliente-milhas-reserva-cancelada");
    }

    @Bean
    public Binding milhasReservaCanceladaErroBinding(Queue milhasReservaCanceladaErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(milhasReservaCanceladaErroQueue).to(exchange).with("saga-ms-cliente-milhas-reserva-cancelada-erro");
    }
}

package br.com.cliente.cliente.consumers.r13_cancelar_voo;

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
    public Queue milhasReservasCancelarVooQueue() {
        return new Queue("ms-cliente-milhas-reservas-cancelar-voo");
    }

    @Bean
    public Queue milhasReservasCanceladasVooCompensarQueue() {
        return new Queue("ms-cliente-milhas-reservas-canceladas-voo-compensar");
    }

    @Bean
    public Binding milhasReservasCancelarVooBinding(Queue milhasReservasCancelarVooQueue, TopicExchange exchange) {
        return BindingBuilder.bind(milhasReservasCancelarVooQueue).to(exchange).with("ms-cliente-milhas-reservas-cancelar-voo");
    }

    @Bean
    public Binding milhasReservasCanceladasVooCompensarBinding(Queue milhasReservasCanceladasVooCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(milhasReservasCanceladasVooCompensarQueue).to(exchange).with("ms-cliente-milhas-reservas-canceladas-voo-compensar");
    }
}

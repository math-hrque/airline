package br.com.cliente.cliente.consumers.r01_cadastrar_cliente_usuario;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class CadastrarClienteQueue {

    @Bean
    public Queue clienteUsuarioCadastrarQueue() {
        return new Queue("ms-cliente-cliente-cadastrar");
    }

    @Bean
    public Queue clienteUsuarioCadastradoCompensarQueue() {
        return new Queue("ms-cliente-cliente-cadastrado-compensar");
    }

    @Bean
    public Binding clienteUsuarioCadastrarBinding(Queue clienteUsuarioCadastrarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(clienteUsuarioCadastrarQueue).to(exchange).with("ms-cliente-cliente-cadastrar");
    }

    @Bean
    public Binding clienteUsuarioCadastradoCompensarBinding(Queue clienteUsuarioCadastradoCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(clienteUsuarioCadastradoCompensarQueue).to(exchange).with("ms-cliente-cliente-cadastrado-compensar");
    }
}

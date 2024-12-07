package br.com.saga.saga.saga.producers.r01_cadastrar_cliente_usuario;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class CadastrarClienteUsuarioQueue {

    @Bean
    public Queue clienteUsuarioCadastradoQueue() {
        return new Queue("saga-ms-cliente-cliente-cadastrado");
    }

    @Bean
    public Queue clienteUsuarioCadastradoEndpointQueue() {
        return new Queue("saga-ms-cliente-cliente-cadastrado-endpoint");
    }

    @Bean
    public Queue clienteUsuarioCadastradoErroQueue() {
        return new Queue("saga-ms-cliente-cliente-cadastrado-erro");
    }

    @Bean
    public Queue usuarioClienteCadastradoQueue() {
        return new Queue("saga-ms-auth-cliente-cadastrado");
    }

    @Bean
    public Queue usuarioClienteCadastradoErroQueue() {
        return new Queue("saga-ms-auth-cliente-cadastrado-erro");
    }

    @Bean
    public Binding clienteUsuarioCadastradoBinding(Queue clienteUsuarioCadastradoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(clienteUsuarioCadastradoQueue).to(exchange).with("saga-ms-cliente-cliente-cadastrado");
    }

    @Bean
    public Binding clienteUsuarioCadastradoEndpointBinding(Queue clienteUsuarioCadastradoEndpointQueue, TopicExchange exchange) {
        return BindingBuilder.bind(clienteUsuarioCadastradoEndpointQueue).to(exchange).with("saga-ms-cliente-cliente-cadastrado-endpoint");
    }

    @Bean
    public Binding clienteUsuarioCadastradoErroBinding(Queue clienteUsuarioCadastradoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(clienteUsuarioCadastradoErroQueue).to(exchange).with("saga-ms-cliente-cliente-cadastrado-erro");
    }

    @Bean
    public Binding usuarioClienteCadastradoBinding(Queue usuarioClienteCadastradoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioClienteCadastradoQueue).to(exchange).with("saga-ms-auth-cliente-cadastrado");
    }

    @Bean
    public Binding usuarioClienteCadastradoErroBinding(Queue usuarioClienteCadastradoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioClienteCadastradoErroQueue).to(exchange).with("saga-ms-auth-cliente-cadastrado-erro");
    }
}

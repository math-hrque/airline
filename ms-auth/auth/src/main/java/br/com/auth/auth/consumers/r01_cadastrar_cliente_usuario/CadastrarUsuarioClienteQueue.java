package br.com.auth.auth.consumers.r01_cadastrar_cliente_usuario;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class CadastrarUsuarioClienteQueue {

    @Bean
    public Queue usuarioClienteCadastrarQueue() {
        return new Queue("ms-auth-cliente-cadastrar");
    }

    @Bean
    public Queue usuarioClienteCadastradoCompensarQueue() {
        return new Queue("ms-auth-cliente-cadastrado-compensar");
    }

    @Bean
    public Binding UsuarioClienteCadastrarBinding(Queue usuarioClienteCadastrarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioClienteCadastrarQueue).to(exchange).with("ms-auth-cliente-cadastrar");
    }

    @Bean
    public Binding usuarioClienteCadastradoCompensarBinding(Queue usuarioClienteCadastradoCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioClienteCadastradoCompensarQueue).to(exchange).with("ms-auth-cliente-cadastrado-compensar");
    }
}

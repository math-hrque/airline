package br.com.auth.auth.consumers.r17_cadastrar_funcionario_usuario;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class CadastrarUsuarioFuncionarioQueue {

    @Bean
    public Queue usuarioFuncionarioCadastrarQueue() {
        return new Queue("ms-auth-funcionario-cadastrar");
    }

    @Bean
    public Queue usuarioFuncionarioCadastradoCompensarQueue() {
        return new Queue("ms-auth-funcionario-cadastrado-compensar");
    }

    @Bean
    public Binding UsuarioFuncionarioCadastrarBinding(Queue usuarioFuncionarioCadastrarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioFuncionarioCadastrarQueue).to(exchange).with("ms-auth-funcionario-cadastrar");
    }

    @Bean
    public Binding usuarioFuncionarioCadastradoCompensarBinding(Queue usuarioFuncionarioCadastradoCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioFuncionarioCadastradoCompensarQueue).to(exchange).with("ms-auth-funcionario-cadastrado-compensar");
    }
}

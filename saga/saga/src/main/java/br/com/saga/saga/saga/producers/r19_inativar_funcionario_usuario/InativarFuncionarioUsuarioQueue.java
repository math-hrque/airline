package br.com.saga.saga.saga.producers.r19_inativar_funcionario_usuario;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class InativarFuncionarioUsuarioQueue {

    @Bean
    public Queue funcionarioUsuarioInativadoQueue() {
        return new Queue("saga-ms-funcionario-funcionario-inativado");
    }

    @Bean
    public Queue funcionarioUsuarioInativadoErroQueue() {
        return new Queue("saga-ms-funcionario-funcionario-inativado-erro");
    }

    @Bean
    public Queue usuarioFuncionarioInativadoQueue() {
        return new Queue("saga-ms-auth-funcionario-inativado");
    }

    @Bean
    public Queue usuarioFuncionarioInativadoErroQueue() {
        return new Queue("saga-ms-auth-funcionario-inativado-erro");
    }

    @Bean
    public Binding funcionarioUsuarioInativadoBinding(Queue funcionarioUsuarioInativadoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioUsuarioInativadoQueue).to(exchange).with("saga-ms-funcionario-funcionario-inativado");
    }

    @Bean
    public Binding funcionarioUsuarioInativadoErroBinding(Queue funcionarioUsuarioInativadoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioUsuarioInativadoErroQueue).to(exchange).with("saga-ms-funcionario-funcionario-inativado-erro");
    }

    @Bean
    public Binding usuarioFuncionarioInativadoBinding(Queue usuarioFuncionarioInativadoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioFuncionarioInativadoQueue).to(exchange).with("saga-ms-auth-funcionario-inativado");
    }

    @Bean
    public Binding usuarioFuncionarioInativadoErroBinding(Queue usuarioFuncionarioInativadoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioFuncionarioInativadoErroQueue).to(exchange).with("saga-ms-auth-funcionario-inativado-erro");
    }
}

package br.com.saga.saga.saga.producers.r18_atualizar_funcionario_usuario;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AtualizarFuncionarioUsuarioQueue {

    @Bean
    public Queue funcionarioUsuarioAtualizadoQueue() {
        return new Queue("saga-ms-funcionario-funcionario-atualizado");
    }

    @Bean
    public Queue funcionarioUsuarioAtualizadoErroQueue() {
        return new Queue("saga-ms-funcionario-funcionario-atualizado-erro");
    }

    @Bean
    public Queue usuarioFuncionarioAtualizadoQueue() {
        return new Queue("saga-ms-auth-funcionario-atualizado");
    }

    @Bean
    public Queue usuarioFuncionarioAtualizadoErroQueue() {
        return new Queue("saga-ms-auth-funcionario-atualizado-erro");
    }

    @Bean
    public Binding funcionarioUsuarioAtualizadoBinding(Queue funcionarioUsuarioAtualizadoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioUsuarioAtualizadoQueue).to(exchange).with("saga-ms-funcionario-funcionario-atualizado");
    }

    @Bean
    public Binding funcionarioUsuarioAtualizadoErroBinding(Queue funcionarioUsuarioAtualizadoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioUsuarioAtualizadoErroQueue).to(exchange).with("saga-ms-funcionario-funcionario-atualizado-erro");
    }

    @Bean
    public Binding usuarioFuncionarioAtualizadoBinding(Queue usuarioFuncionarioAtualizadoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioFuncionarioAtualizadoQueue).to(exchange).with("saga-ms-auth-funcionario-atualizado");
    }

    @Bean
    public Binding usuarioFuncionarioAtualizadoErroBinding(Queue usuarioFuncionarioAtualizadoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioFuncionarioAtualizadoErroQueue).to(exchange).with("saga-ms-auth-funcionario-atualizado-erro");
    }
}

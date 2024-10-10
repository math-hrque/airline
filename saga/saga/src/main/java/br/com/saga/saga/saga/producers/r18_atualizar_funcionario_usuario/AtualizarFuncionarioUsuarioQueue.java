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
    public Queue funcionarioAtualizadoQueue() {
        return new Queue("ms-funcionario-atualizado");
    }

    @Bean
    public Queue funcionarioAtualizadoErroQueue() {
        return new Queue("ms-funcionario-atualiza-erro");
    }

    @Bean
    public Queue usuarioAtualizadoQueue() {
        return new Queue("ms-auth-atualizado");
    }

    @Bean
    public Queue usuarioAtualizadoErroQueue() {
        return new Queue("ms-auth-atualiza-erro");
    }

    @Bean
    public Binding funcionarioAtualizadoBinding(Queue funcionarioAtualizadoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioAtualizadoQueue).to(exchange).with("ms-funcionario-atualizado");
    }

    @Bean
    public Binding funcionarioAtualizadoErroBinding(Queue funcionarioAtualizadoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioAtualizadoErroQueue).to(exchange).with("ms-funcionario-atualizado-erro");
    }

    @Bean
    public Binding usuarioAtualizadoBinding(Queue usuarioAtualizadoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioAtualizadoQueue).to(exchange).with("ms-auth-atualizado");
    }

    @Bean
    public Binding usuarioAtualizadoErroBinding(Queue usuarioAtualizadoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioAtualizadoErroQueue).to(exchange).with("ms-auth-atualizado-erro");
    }

}

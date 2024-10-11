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
    public Queue funcionarioInativadoQueue() {
        return new Queue("ms-funcionario-inativado");
    }

    @Bean
    public Queue funcionarioInativoErroQueue() {
        return new Queue("ms-funcionario-inativo-erro");
    }

    @Bean
    public Queue usuarioInativadoQueue() {
        return new Queue("ms-auth-inativado");
    }

    @Bean
    public Queue usuarioInativoErroQueue() {
        return new Queue("ms-auth-inativo-erro");
    }

    @Bean
    public Binding funcionarioInativadoBinding(Queue funcionarioInativadoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioInativadoQueue).to(exchange).with("ms-funcionario-inativado");
    }

    @Bean
    public Binding funcionarioInativoErroBinding(Queue funcionarioInativoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioInativoErroQueue).to(exchange).with("ms-funcionario-inativo-erro");
    }

    @Bean
    public Binding usuarioInativadoBinding(Queue usuarioInativadoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioInativadoQueue).to(exchange).with("ms-auth-inativado");
    }

    @Bean
    public Binding usuarioInativoErroBinding(Queue usuarioInativoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioInativoErroQueue).to(exchange).with("ms-auth-inativo-erro");
    }
}

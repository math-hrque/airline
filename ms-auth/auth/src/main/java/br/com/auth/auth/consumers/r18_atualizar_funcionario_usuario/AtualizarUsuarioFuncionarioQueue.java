package br.com.auth.auth.consumers.r18_atualizar_funcionario_usuario;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AtualizarUsuarioFuncionarioQueue {

    @Bean
    public Queue usuarioFuncionarioAtualizarQueue() {
        return new Queue("ms-auth-funcionario-atualizar");
    }

    @Bean
    public Queue usuarioFuncionarioAtualizadoCompensarQueue() {
        return new Queue("ms-auth-funcionario-atualizado-compensar");
    }

    @Bean
    public Binding usuarioFuncionarioAtualizarBinding(Queue usuarioFuncionarioAtualizarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioFuncionarioAtualizarQueue).to(exchange).with("ms-auth-funcionario-atualizar");
    }

    @Bean
    public Binding usuarioFuncionarioAtualizadoCompensarBinding(Queue usuarioFuncionarioAtualizadoCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioFuncionarioAtualizadoCompensarQueue).to(exchange).with("ms-auth-funcionario-atualizado-compensar");
    }
}

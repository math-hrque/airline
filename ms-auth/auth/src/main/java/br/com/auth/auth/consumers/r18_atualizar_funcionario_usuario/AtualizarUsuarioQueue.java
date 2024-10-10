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
public class AtualizarUsuarioQueue {

    @Bean
    public Queue usuarioAtualizarQueue() {
        return new Queue("ms-auth-atualizar");
    }

    @Bean
    public Queue usuarioAtualizaCompensarEmailQueue() {
        return new Queue("ms-auth-atualiza-compensar-email");
    }

    @Bean
    public Binding atualizarUsuarioBinding(Queue usuarioAtualizarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioAtualizarQueue).to(exchange).with("ms-auth-atualizar");
    }

    @Bean
    public Binding usuarioAtualizaCompensarEmailBinding(Queue usuarioAtualizaCompensarEmailQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioAtualizaCompensarEmailQueue).to(exchange).with("ms-auth-atualiza-compensar-email");
    }

}

package br.com.auth.auth.consumers.r19_inativar_funcionario_usuario;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class InativarUsuarioFuncionarioQueue {

    @Bean
    public Queue usuarioFuncionarioInativarQueue() {
        return new Queue("ms-auth-funcionario-inativar");
    }

    @Bean
    public Queue usuarioFuncionarioInativadoCompensarQueue() {
        return new Queue("ms-auth-funcionario-inativado-compensar");
    }

    @Bean
    public Binding usuarioFuncionarioInativarBinding(Queue usuarioFuncionarioInativarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioFuncionarioInativarQueue).to(exchange).with("ms-auth-funcionario-inativar");
    }

    @Bean
    public Binding usuariofuncionarioInativadoCompensarBinding(Queue usuarioFuncionarioInativadoCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioFuncionarioInativadoCompensarQueue).to(exchange).with("ms-auth-funcionario-inativado-compensar");
    }
}

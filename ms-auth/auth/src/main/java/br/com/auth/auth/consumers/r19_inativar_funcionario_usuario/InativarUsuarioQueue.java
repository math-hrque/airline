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
public class InativarUsuarioQueue {

    @Bean
    public Queue usuarioInativarQueue() {
        return new Queue("ms-auth-inativar");
    }

    @Bean
    public Queue usuarioInativoCompensarEmailQueue() {
        return new Queue("ms-auth-inativo-compensar-email");
    }

    @Bean
    public Binding inativarUsuarioBinding(Queue usuarioInativarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioInativarQueue).to(exchange).with("ms-auth-inativar");
    }

    @Bean
    public Binding usuarioInativoCompensarEmailBinding(Queue usuarioInativoCompensarEmailQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioInativoCompensarEmailQueue).to(exchange).with("ms-auth-inativo-compensar-email");
    }
}

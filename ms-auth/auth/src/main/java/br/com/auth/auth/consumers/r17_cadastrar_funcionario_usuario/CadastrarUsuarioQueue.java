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
public class CadastrarUsuarioQueue {

    @Bean
    public Queue usuarioCadastrarQueue() {
        return new Queue("ms-auth-cadastrar");
    }

    @Bean
    public Queue usuarioCadastroCompensarEmailQueue() {
        return new Queue("ms-auth-cadastro-compensar-email");
    }

    @Bean
    public Binding cadastrarUsuarioBinding(Queue usuarioCadastrarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioCadastrarQueue).to(exchange).with("ms-auth-cadastrar");
    }

    @Bean
    public Binding usuarioCadastroCompensarEmailBinding(Queue usuarioCadastroCompensarEmailQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioCadastroCompensarEmailQueue).to(exchange).with("ms-auth-cadastro-compensar-email");
    }

}

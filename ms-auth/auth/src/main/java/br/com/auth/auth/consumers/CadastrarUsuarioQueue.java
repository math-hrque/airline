package br.com.auth.auth.consumers;

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

    public static final String EXCHANGE_NAME = "saga-exchange";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue usuarioCadastrarQueue() {
        return new Queue("ms-auth-cadastrar");
    }

    @Bean
    public Queue compensarUsuarioQueue() {
        return new Queue("ms-auth-compensar-email");
    }

    @Bean
    public Binding cadastrarUsuarioBinding(Queue usuarioCadastrarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioCadastrarQueue).to(exchange).with("ms-auth-cadastrar");
    }

    @Bean
    public Binding compensarUsuarioBinding(Queue compensarUsuarioQueue, TopicExchange exchange) {
        return BindingBuilder.bind(compensarUsuarioQueue).to(exchange).with("ms-auth-compensar-email");
    }

}

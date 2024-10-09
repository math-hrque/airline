package br.com.funcionario.funcionario.consumers.r17_cadastrar_funcionario_usuario;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class CadastrarFuncionarioQueue {

    public static final String EXCHANGE_NAME = "saga-exchange";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue funcionarioCadastrarQueue() {
        return new Queue("ms-funcionario-cadastrar");
    }

    @Bean
    public Queue compensarFuncionarioQueue() {
        return new Queue("ms-funcionario-compensar-email");
    }

    @Bean
    public Binding cadastrarFuncionarioBinding(Queue funcionarioCadastrarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioCadastrarQueue).to(exchange).with("ms-funcionario-cadastrar");
    }

    @Bean
    public Binding compensarFuncionarioBinding(Queue compensarFuncionarioQueue, TopicExchange exchange) {
        return BindingBuilder.bind(compensarFuncionarioQueue).to(exchange).with("ms-funcionario-compensar-email");
    }

}

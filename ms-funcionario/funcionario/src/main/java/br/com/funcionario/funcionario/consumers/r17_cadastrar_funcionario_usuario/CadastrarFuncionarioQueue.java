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

    @Bean
    public Queue funcionarioUsuarioCadastrarQueue() {
        return new Queue("ms-funcionario-funcionario-cadastrar");
    }

    @Bean
    public Queue funcionarioUsuarioCadastradoCompensarQueue() {
        return new Queue("ms-funcionario-funcionario-cadastrado-compensar");
    }

    @Bean
    public Binding funcionarioUsuarioCadastrarBinding(Queue funcionarioUsuarioCadastrarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioUsuarioCadastrarQueue).to(exchange).with("ms-funcionario-funcionario-cadastrar");
    }

    @Bean
    public Binding funcionarioUsuarioCadastradoCompensarBinding(Queue funcionarioUsuarioCadastradoCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioUsuarioCadastradoCompensarQueue).to(exchange).with("ms-funcionario-funcionario-cadastrado-compensar");
    }
}

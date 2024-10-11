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
    public Queue funcionarioCadastrarQueue() {
        return new Queue("ms-funcionario-cadastrar");
    }

    @Bean
    public Queue funcionarioCadastroCompensarEmailQueue() {
        return new Queue("ms-funcionario-cadastro-compensar-email");
    }

    @Bean
    public Binding funcionarioCadastrarBinding(Queue funcionarioCadastrarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioCadastrarQueue).to(exchange).with("ms-funcionario-cadastrar");
    }

    @Bean
    public Binding funcionarioCadastroCompensarEmailBinding(Queue funcionarioCadastroCompensarEmailQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioCadastroCompensarEmailQueue).to(exchange).with("ms-funcionario-cadastro-compensar-email");
    }
}

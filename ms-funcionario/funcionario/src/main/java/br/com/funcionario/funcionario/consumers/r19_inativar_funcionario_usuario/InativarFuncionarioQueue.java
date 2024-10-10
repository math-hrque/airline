package br.com.funcionario.funcionario.consumers.r19_inativar_funcionario_usuario;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class InativarFuncionarioQueue {

    @Bean
    public Queue funcionarioInativarQueue() {
        return new Queue("ms-funcionario-inativar");
    }

    @Bean
    public Queue funcionarioInativoCompensarEmailQueue() {
        return new Queue("ms-funcionario-inativo-compensar-email");
    }

    @Bean
    public Binding funcionarioInativarBinding(Queue funcionarioInativarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioInativarQueue).to(exchange).with("ms-funcionario-inativar");
    }

    @Bean
    public Binding funcionarioInativoCompensarEmailBinding(Queue funcionarioInativoCompensarEmailQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioInativoCompensarEmailQueue).to(exchange).with("ms-funcionario-inativo-compensar-email");
    }

}

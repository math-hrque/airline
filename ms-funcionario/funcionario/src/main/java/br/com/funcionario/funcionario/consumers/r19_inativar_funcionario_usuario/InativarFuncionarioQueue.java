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
    public Queue funcionarioUsuarioInativarQueue() {
        return new Queue("ms-funcionario-funcionario-inativar");
    }

    @Bean
    public Queue funcionarioUsuarioInativadoCompensarQueue() {
        return new Queue("ms-funcionario-funcionario-inativado-compensar");
    }

    @Bean
    public Binding funcionarioUsuarioInativarBinding(Queue funcionarioUsuarioInativarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioUsuarioInativarQueue).to(exchange).with("ms-funcionario-funcionario-inativar");
    }

    @Bean
    public Binding funcionarioUsuarioInativadoCompensarBinding(Queue funcionarioUsuarioInativadoCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioUsuarioInativadoCompensarQueue).to(exchange).with("ms-funcionario-funcionario-inativado-compensar");
    }
}

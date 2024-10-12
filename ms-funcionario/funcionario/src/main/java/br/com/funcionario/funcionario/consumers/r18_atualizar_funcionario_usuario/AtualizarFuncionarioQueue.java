package br.com.funcionario.funcionario.consumers.r18_atualizar_funcionario_usuario;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AtualizarFuncionarioQueue {

    @Bean
    public Queue funcionarioUsuarioAtualizarQueue() {
        return new Queue("ms-funcionario-funcionario-atualizar");
    }

    @Bean
    public Queue funcionarioUsuarioAtualizadoCompensarQueue() {
        return new Queue("ms-funcionario-funcionario-atualizado-compensar");
    }

    @Bean
    public Binding funcionarioUsuarioAtualizarBinding(Queue funcionarioUsuarioAtualizarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioUsuarioAtualizarQueue).to(exchange).with("ms-funcionario-funcionario-atualizar");
    }

    @Bean
    public Binding funcionarioUsuarioAtualizadoCompensarBinding(Queue funcionarioUsuarioAtualizadoCompensarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioUsuarioAtualizadoCompensarQueue).to(exchange).with("ms-funcionario-funcionario-atualizado-compensar");
    }
}

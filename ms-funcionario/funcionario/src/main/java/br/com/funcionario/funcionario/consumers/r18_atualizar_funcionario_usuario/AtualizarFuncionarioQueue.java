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
    public Queue funcionarioAtualizarQueue() {
        return new Queue("ms-funcionario-atualizar");
    }

    @Bean
    public Queue funcionarioAtualizaCompensarIdFuncionarioQueue() {
        return new Queue("ms-funcionario-atualiza-compensar-idFuncionario");
    }

    @Bean
    public Binding funcionarioAtualizarBinding(Queue funcionarioAtualizarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioAtualizarQueue).to(exchange).with("ms-funcionario-atualizar");
    }

    @Bean
    public Binding funcionarioAtualizaCompensarIdFuncionarioBinding(Queue funcionarioAtualizaCompensarIdFuncionarioQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioAtualizaCompensarIdFuncionarioQueue).to(exchange).with("ms-funcionario-atualiza-compensar-idFuncionario");
    }
}

package br.com.saga.saga.saga.producers.r17_cadastrar_funcionario_usuario;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class CadastrarFuncionarioUsuarioQueue {

    @Bean
    public Queue funcionarioUsuarioCadastradoQueue() {
        return new Queue("saga-ms-funcionario-funcionario-cadastrado");
    }

    @Bean
    public Queue funcionarioUsuarioCadastradoErroQueue() {
        return new Queue("saga-ms-funcionario-funcionario-cadastrado-erro");
    }

    @Bean
    public Queue usuarioFuncionarioCadastradoQueue() {
        return new Queue("saga-ms-auth-funcionario-cadastrado");
    }

    @Bean
    public Queue usuarioFuncionarioCadastradoErroQueue() {
        return new Queue("saga-ms-auth-funcionario-cadastrado-erro");
    }

    @Bean
    public Binding funcionarioUsuarioCadastradoBinding(Queue funcionarioUsuarioCadastradoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioUsuarioCadastradoQueue).to(exchange).with("saga-ms-funcionario-funcionario-cadastrado");
    }

    @Bean
    public Binding funcionarioUsuarioCadastradoErroBinding(Queue funcionarioUsuarioCadastradoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioUsuarioCadastradoErroQueue).to(exchange).with("saga-ms-funcionario-funcionario-cadastrado-erro");
    }

    @Bean
    public Binding usuarioFuncionarioCadastradoBinding(Queue usuarioFuncionarioCadastradoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioFuncionarioCadastradoQueue).to(exchange).with("saga-ms-auth-funcionario-cadastrado");
    }

    @Bean
    public Binding usuarioFuncionarioCadastradoErroBinding(Queue usuarioFuncionarioCadastradoErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioFuncionarioCadastradoErroQueue).to(exchange).with("saga-ms-auth-funcionario-cadastrado-erro");
    }
}

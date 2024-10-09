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

    public static final String EXCHANGE_NAME = "saga-exchange";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue funcionarioCadastradoQueue() {
        return new Queue("ms-funcionario-cadastrado");
    }

    @Bean
    public Queue funcionarioCadastroErroQueue() {
        return new Queue("ms-funcionario-cadastro-erro");
    }

    @Bean
    public Queue usuarioCadastradoQueue() {
        return new Queue("ms-auth-cadastrado");
    }

    @Bean
    public Queue usuarioCadastroErroQueue() {
        return new Queue("ms-auth-cadastro-erro");
    }

    @Bean
    public Binding funcionarioCadastradoBinding(Queue funcionarioCadastradoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioCadastradoQueue).to(exchange).with("ms-funcionario-cadastrado");
    }

    @Bean
    public Binding funcionarioCadastroErroBinding(Queue funcionarioCadastroErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funcionarioCadastroErroQueue).to(exchange).with("ms-funcionario-cadastro-erro");
    }

    @Bean
    public Binding usuarioCadastradoBinding(Queue usuarioCadastradoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioCadastradoQueue).to(exchange).with("ms-auth-cadastrado");
    }

    @Bean
    public Binding usuarioCadastroErroBinding(Queue usuarioCadastroErroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usuarioCadastroErroQueue).to(exchange).with("ms-auth-cadastro-erro");
    }

}

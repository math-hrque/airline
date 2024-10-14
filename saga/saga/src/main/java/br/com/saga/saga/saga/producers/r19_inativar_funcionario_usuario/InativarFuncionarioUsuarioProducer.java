package br.com.saga.saga.saga.producers.r19_inativar_funcionario_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.saga.saga.saga.dtos.UsuarioResponseDto;

@Component
public class InativarFuncionarioUsuarioProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "saga-ms-funcionario-funcionario-inativado")
    public void funcionarioInativarListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-funcionario-inativar", email);
    }

    @RabbitListener(queues = "saga-ms-funcionario-funcionario-inativado-erro")
    public void funcionarioInativadoErroListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-funcionario-inativado-compensar", email);
    }

    @RabbitListener(queues = "saga-ms-auth-funcionario-inativado")
    public void usuarioInativadoListener(UsuarioResponseDto usuarioResponseDto) {
        System.out.println("Funcionario e Usuario inativados com sucesso!");
    }

    @RabbitListener(queues = "saga-ms-auth-funcionario-inativado-erro")
    public void usuarioInativadoErroListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-funcionario-inativado-compensar", email);
    }
}

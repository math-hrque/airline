package br.com.saga.saga.saga.producers.r19_inativar_funcionario_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.saga.saga.saga.dtos.UsuarioResponseDto;

@Component
@RestController
@RequestMapping("/api/saga/funcionario")
public class InativarFuncionarioUsuarioProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @DeleteMapping("/inativar-funcionario/{email}")
    public void inativarFuncionario(@PathVariable("email") String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-inativar", email);
    }

    @RabbitListener(queues = "ms-funcionario-inativado")
    public void funcionarioInativadoListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-inativar", email);
    }

    @RabbitListener(queues = "ms-funcionario-inativo-erro")
    public void funcionarioInativoErroListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-inativo-compensar-email", email);
    }

    @RabbitListener(queues = "ms-auth-inativado")
    public void usuarioInativadoListener(UsuarioResponseDto usuarioResponseDto) {
        System.out.println("Funcionário e Usuário inativados com sucesso!");
    }

    @RabbitListener(queues = "ms-auth-inativo-erro")
    public void usuarioInativoErroListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-inativo-compensar-email", email);
    }
}

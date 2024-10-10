package br.com.saga.saga.saga.producers.r18_atualizar_funcionario_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.saga.saga.saga.dtos.FuncionarioRequestDto;
import br.com.saga.saga.saga.dtos.UsuarioRequestDto;
import br.com.saga.saga.saga.dtos.UsuarioResponseDto;
import jakarta.validation.Valid;

@Component
@RestController
@RequestMapping("/api/saga/funcionario")
public class AtualizarFuncionarioUsuarioProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @PutMapping("/atualizar-funcionario")
    public void atualizarFuncionario(@RequestBody @Valid FuncionarioRequestDto funcionarioRequestDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-atualizar", funcionarioRequestDto);
    }

    @RabbitListener(queues = "ms-funcionario-atualizado")
    public void funcionarioAtualizadoListener(UsuarioRequestDto usuarioRequestDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-atualizar", usuarioRequestDto);
    }

    @RabbitListener(queues = "ms-funcionario-atualiza-erro")
    public void funcionarioAtualizaErroListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-atualiza-compensar-email", email);
    }

    @RabbitListener(queues = "ms-auth-atualizado")
    public void usuarioAtualizadoListener(UsuarioResponseDto usuarioResponseDto) {
        System.out.println("Funcionário e Usuário atualizados com sucesso!");
    }

    @RabbitListener(queues = "ms-auth-atualiza-erro")
    public void usuarioAtualizaErroListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-atualiza-compensar-email", email);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-atualiza-compensar-email", email);
    }

}

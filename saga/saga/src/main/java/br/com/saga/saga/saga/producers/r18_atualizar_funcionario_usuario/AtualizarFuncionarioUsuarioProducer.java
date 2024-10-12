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
import br.com.saga.saga.saga.dtos.UsuarioIdRequestDto;
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
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-funcionario-atualizar", funcionarioRequestDto);
    }

    @RabbitListener(queues = "saga-ms-funcionario-funcionario-atualizado")
    public void funcionarioAtualizadoListener(UsuarioIdRequestDto usuarioIdRequestDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-funcionario-atualizar", usuarioIdRequestDto);
    }

    @RabbitListener(queues = "saga-ms-funcionario-funcionario-atualizado-erro")
    public void funcionarioAtualizaErroListener(Long idFuncionario) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-funcionario-atualizado-compensar", idFuncionario);
    }

    @RabbitListener(queues = "saga-ms-auth-funcionario-atualizado")
    public void usuarioAtualizadoListener(UsuarioResponseDto usuarioResponseDto) {
        System.out.println("Funcionario e Usuario atualizados com sucesso!");
    }

    @RabbitListener(queues = "saga-ms-auth-funcionario-atualizado-erro")
    public void usuarioAtualizaErroListener(UsuarioIdRequestDto usuarioIdRequestDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-funcionario-atualizado-compensar", usuarioIdRequestDto.getEmail());
    }
}

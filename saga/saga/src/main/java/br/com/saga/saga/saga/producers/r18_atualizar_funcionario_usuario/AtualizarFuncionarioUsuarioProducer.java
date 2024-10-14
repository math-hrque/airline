package br.com.saga.saga.saga.producers.r18_atualizar_funcionario_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.saga.saga.saga.dtos.UsuarioRequestAtualizarDto;
import br.com.saga.saga.saga.dtos.UsuarioResponseDto;

@Component
public class AtualizarFuncionarioUsuarioProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "saga-ms-funcionario-funcionario-atualizado")
    public void funcionarioAtualizadoListener(UsuarioRequestAtualizarDto usuarioRequestAtualizarDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-funcionario-atualizar", usuarioRequestAtualizarDto);
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
    public void usuarioAtualizaErroListener(UsuarioRequestAtualizarDto usuarioRequestAtualizarDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-funcionario-atualizado-compensar", usuarioRequestAtualizarDto.getEmail());
    }
}

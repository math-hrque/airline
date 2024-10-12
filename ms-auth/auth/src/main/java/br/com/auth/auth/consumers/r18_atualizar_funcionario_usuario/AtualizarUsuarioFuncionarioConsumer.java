package br.com.auth.auth.consumers.r18_atualizar_funcionario_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.auth.auth.dtos.UsuarioRequestAtualizarDto;
import br.com.auth.auth.dtos.UsuarioResponseDto;
import br.com.auth.auth.exeptions.OutroUsuarioDadosJaExistenteException;
import br.com.auth.auth.exeptions.UsuarioNaoExisteException;
import br.com.auth.auth.services.AuthService;

@Component
public class AtualizarUsuarioFuncionarioConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    AuthService authService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-auth-funcionario-atualizar")
    public void atualizarUsuario(UsuarioRequestAtualizarDto usuarioRequestAtualizarDto) {
        try {
            UsuarioResponseDto usuarioResponse = authService.atualizar(usuarioRequestAtualizarDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-auth-funcionario-atualizado", usuarioResponse);
        } catch (UsuarioNaoExisteException e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-funcionario-funcionario-atualizado-erro", usuarioRequestAtualizarDto.getId());
        } catch (OutroUsuarioDadosJaExistenteException e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-funcionario-funcionario-atualizado-erro", usuarioRequestAtualizarDto.getId());
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-funcionario-funcionario-atualizado-erro", usuarioRequestAtualizarDto.getId());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-auth-funcionario-atualizado-erro", usuarioRequestAtualizarDto);
        }
    }

    @RabbitListener(queues = "ms-auth-funcionario-atualizado-compensar")
    public void compensarUsuarioAtualizado(String email) {
        try {
            authService.reverter(email);
        } catch (UsuarioNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

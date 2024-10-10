package br.com.auth.auth.consumers.r18_atualizar_funcionario_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.auth.auth.dtos.UsuarioRequestDto;
import br.com.auth.auth.dtos.UsuarioResponseDto;
import br.com.auth.auth.exeptions.OutroUsuarioDadosJaExistente;
import br.com.auth.auth.exeptions.UsuarioNaoExisteException;
import br.com.auth.auth.services.AuthService;

@Component
public class AtualizarUsuarioConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    AuthService authService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-auth-atualizar")
    public void atualizarUsuario(UsuarioRequestDto usuarioRequestDto) {
        try {
            UsuarioResponseDto usuarioResponse = authService.atualizar(usuarioRequestDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-atualizado", usuarioResponse);
        } catch (UsuarioNaoExisteException e) {

        } catch (OutroUsuarioDadosJaExistente e) {

        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-atualiza-erro", usuarioRequestDto.getEmail());
        }
    }

    @RabbitListener(queues = "ms-auth-atualiza-compensar-email")
    public void compensarAtualizaFuncionario(String email) {
        try {
            authService.remover(email);
        } catch (UsuarioNaoExisteException e) {

        } catch (Exception e) {

        }
    }

}

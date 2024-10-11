package br.com.auth.auth.consumers.r18_atualizar_funcionario_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.auth.auth.dtos.UsuarioIdRequestDto;
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
    public void atualizarUsuario(UsuarioIdRequestDto usuarioIdRequestDto) {
        try {
            UsuarioResponseDto usuarioResponse = authService.atualizar(usuarioIdRequestDto);
        } catch (UsuarioNaoExisteException e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-atualiza-erro", usuarioIdRequestDto.getId());
        } catch (OutroUsuarioDadosJaExistente e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-atualiza-erro", usuarioIdRequestDto.getId());
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-atualiza-erro", usuarioIdRequestDto.getId());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-atualiza-erro", usuarioIdRequestDto);
        }
    }

    @RabbitListener(queues = "ms-auth-atualiza-compensar-email")
    public void compensarAtualizaFuncionario(String email) {
        try {
            authService.reverter(email);
        } catch (UsuarioNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

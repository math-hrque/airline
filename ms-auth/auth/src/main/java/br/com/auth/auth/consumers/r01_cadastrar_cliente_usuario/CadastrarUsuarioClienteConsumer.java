package br.com.auth.auth.consumers.r01_cadastrar_cliente_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.auth.auth.dtos.UsuarioRequestDto;
import br.com.auth.auth.dtos.UsuarioResponseDto;
import br.com.auth.auth.exeptions.OutroUsuarioDadosJaExistenteException;
import br.com.auth.auth.exeptions.UsuarioNaoExisteException;
import br.com.auth.auth.services.AuthService;

@Component
public class CadastrarUsuarioClienteConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    AuthService authService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-auth-cliente-cadastrar")
    public void cadastrarUsuario(UsuarioRequestDto usuarioRequestDto) {
        try {
            UsuarioResponseDto usuarioResponse = authService.cadastrar(usuarioRequestDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-auth-cliente-cadastrado", usuarioResponse);
        } catch (OutroUsuarioDadosJaExistenteException e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-cliente-cadastrado-erro", usuarioRequestDto.getEmail());
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-cliente-cadastrado-erro", usuarioRequestDto.getEmail());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-auth-cliente-cadastrado-erro", usuarioRequestDto.getEmail());
        }
    }

    @RabbitListener(queues = "ms-auth-cliente-cadastrado-compensar")
    public void compensarUsuarioCadastrado(String email) {
        try {
            authService.remover(email);
        } catch (UsuarioNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

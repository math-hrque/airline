package br.com.auth.auth.consumers.r17_cadastrar_funcionario_usuario;

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
public class CadastrarUsuarioConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    AuthService authService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-auth-cadastrar")
    public void cadastrarUsuario(UsuarioRequestDto usuarioRequestDto) {
        try {
            UsuarioResponseDto usuarioResponse = authService.cadastrar(usuarioRequestDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-cadastrado", usuarioResponse);
        } catch (OutroUsuarioDadosJaExistente e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-cadastro-erro", usuarioRequestDto.getEmail());
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-cadastro-erro", usuarioRequestDto.getEmail());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-cadastro-erro", usuarioRequestDto.getEmail());
        }
    }

    @RabbitListener(queues = "ms-auth-cadastro-compensar-email")
    public void compensarCadastroFuncionario(String email) {
        try {
            authService.remover(email);
        } catch (UsuarioNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

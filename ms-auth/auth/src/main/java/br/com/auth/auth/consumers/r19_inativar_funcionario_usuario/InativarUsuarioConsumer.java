package br.com.auth.auth.consumers.r19_inativar_funcionario_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.auth.auth.exeptions.UsuarioNaoExisteException;
import br.com.auth.auth.services.AuthService;

@Component
public class InativarUsuarioConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    AuthService authService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-auth-inativar")
    public void inativarUsuario(String email) {
        try {
            authService.inativar(email);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-inativado", email);
        } catch (UsuarioNaoExisteException e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-inativo-erro", email);
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-inativo-erro", email);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-inativo-erro", email);
        }
    }

    @RabbitListener(queues = "ms-auth-inativo-compensar-email")
    public void compensarInativoFuncionario(String email) {
        try {
            authService.ativar(email);
        } catch (UsuarioNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

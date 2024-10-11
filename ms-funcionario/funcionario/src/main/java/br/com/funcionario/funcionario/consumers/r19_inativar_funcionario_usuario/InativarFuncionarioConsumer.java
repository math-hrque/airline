package br.com.funcionario.funcionario.consumers.r19_inativar_funcionario_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.funcionario.funcionario.exeptions.FuncionarioNaoExisteException;
import br.com.funcionario.funcionario.services.FuncionarioService;

@Component
public class InativarFuncionarioConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    FuncionarioService funcionarioService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-funcionario-inativar")
    public void inativarFuncionario(String email) {
        try {
            funcionarioService.inativar(email);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-inativado", email);
        } catch (FuncionarioNaoExisteException e) {

        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-inativo-erro", email);
        }
    }

    @RabbitListener(queues = "ms-funcionario-inativo-compensar-email")
    public void compensarInativoFuncionario(String email) {
        try {
            funcionarioService.ativar(email);
        } catch (FuncionarioNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

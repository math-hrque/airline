package br.com.funcionario.funcionario.consumers.r17_cadastrar_funcionario_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.funcionario.funcionario.dtos.FuncionarioRequestDto;
import br.com.funcionario.funcionario.dtos.UsuarioRequestDto;
import br.com.funcionario.funcionario.exeptions.FuncionarioNaoExisteException;
import br.com.funcionario.funcionario.exeptions.OutroFuncionarioDadosJaExistente;
import br.com.funcionario.funcionario.services.FuncionarioService;

@Component
public class CadastrarFuncionarioConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    FuncionarioService funcionarioService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-funcionario-cadastrar")
    public void cadastrarFuncionario(FuncionarioRequestDto funcionarioRequestDto) {
        try {
            UsuarioRequestDto usuarioRequestDto = funcionarioService.cadastrar(funcionarioRequestDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-cadastrado", usuarioRequestDto);
        } catch (OutroFuncionarioDadosJaExistente e) {

        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-cadastro-erro", funcionarioRequestDto.getEmail());
        }
    }

    @RabbitListener(queues = "ms-funcionario-cadastro-compensar-email")
    public void compensarCadastroFuncionario(String email) {
        try {
            funcionarioService.remover(email);
        } catch (FuncionarioNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

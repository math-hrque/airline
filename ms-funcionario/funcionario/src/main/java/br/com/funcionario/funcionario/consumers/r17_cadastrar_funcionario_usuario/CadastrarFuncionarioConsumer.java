package br.com.funcionario.funcionario.consumers.r17_cadastrar_funcionario_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.funcionario.funcionario.dtos.FuncionarioRequestDto;
import br.com.funcionario.funcionario.dtos.UsuarioRequestCadastrarDto;
import br.com.funcionario.funcionario.exeptions.FuncionarioNaoExisteException;
import br.com.funcionario.funcionario.exeptions.OutroFuncionarioDadosJaExistenteException;
import br.com.funcionario.funcionario.services.FuncionarioService;

@Component
public class CadastrarFuncionarioConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    FuncionarioService funcionarioService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-funcionario-funcionario-cadastrar")
    public void cadastrarFuncionario(FuncionarioRequestDto funcionarioRequestDto) {
        try {
            UsuarioRequestCadastrarDto usuarioRequestCadastrarDto = funcionarioService.cadastrar(funcionarioRequestDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-funcionario-funcionario-cadastrado", usuarioRequestCadastrarDto);
        } catch (OutroFuncionarioDadosJaExistenteException e) {

        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-funcionario-funcionario-cadastrado-erro", funcionarioRequestDto.getEmail());
        }
    }

    @RabbitListener(queues = "ms-funcionario-funcionario-cadastrado-compensar")
    public void compensarFuncionarioCadastrado(String email) {
        try {
            funcionarioService.remover(email);
        } catch (FuncionarioNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

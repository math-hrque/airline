package br.com.funcionario.funcionario.consumers.r18_atualizar_funcionario_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.funcionario.funcionario.dtos.FuncionarioRequestDto;
import br.com.funcionario.funcionario.dtos.UsuarioRequestAtualizarDto;
import br.com.funcionario.funcionario.exeptions.FuncionarioNaoExisteException;
import br.com.funcionario.funcionario.exeptions.OutroFuncionarioDadosJaExistenteException;
import br.com.funcionario.funcionario.services.FuncionarioService;

@Component
public class AtualizarFuncionarioConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    FuncionarioService funcionarioService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-funcionario-funcionario-atualizar")
    public void atualizarFuncionario(FuncionarioRequestDto funcionarioRequestDto) {
        try {
            UsuarioRequestAtualizarDto usuarioRequestAtualizarDto = funcionarioService.atualizar(funcionarioRequestDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-funcionario-funcionario-atualizado", usuarioRequestAtualizarDto);
        } catch (FuncionarioNaoExisteException e) {

        } catch (OutroFuncionarioDadosJaExistenteException e) {
        
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-funcionario-funcionario-atualizado-erro", funcionarioRequestDto.getIdFuncionario());
        }
    }

    @RabbitListener(queues = "ms-funcionario-funcionario-atualizado-compensar")
    public void compensarFuncionarioAtualizado(Long idFuncionario) {
        try {
            funcionarioService.reverter(idFuncionario);
        } catch (FuncionarioNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

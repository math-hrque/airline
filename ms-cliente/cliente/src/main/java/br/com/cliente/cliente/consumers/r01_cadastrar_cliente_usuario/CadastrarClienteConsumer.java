package br.com.cliente.cliente.consumers.r01_cadastrar_cliente_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cliente.cliente.dtos.ClienteRequestDto;
import br.com.cliente.cliente.dtos.UsuarioRequestDto;
import br.com.cliente.cliente.exeptions.ClienteNaoExisteException;
import br.com.cliente.cliente.exeptions.OutroClienteDadosJaExistenteException;
import br.com.cliente.cliente.services.ClienteService;

@Component
public class CadastrarClienteConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ClienteService clienteService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-cliente-cliente-cadastrar")
    public void cadastrarCliente(ClienteRequestDto clienteRequestDto) {
        try {
            UsuarioRequestDto usuarioRequestDto = clienteService.cadastrar(clienteRequestDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-cliente-cadastrado", usuarioRequestDto);
        } catch (OutroClienteDadosJaExistenteException e) {

        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-cliente-cadastrado-erro", clienteRequestDto.getEmail());
        }
    }

    @RabbitListener(queues = "ms-cliente-cliente-cadastrado-compensar")
    public void compensarClienteCadastrado(String email) {
        try {
            clienteService.remover(email);
        } catch (ClienteNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

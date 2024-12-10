package br.com.cliente.cliente.consumers.r01_cadastrar_cliente_usuario;

import br.com.cliente.cliente.dtos.UsuarioRequestResponseDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cliente.cliente.dtos.ClienteDto;
import br.com.cliente.cliente.dtos.UsuarioRequestCadastrarDto;
import br.com.cliente.cliente.exeptions.ClienteNaoExisteException;
import br.com.cliente.cliente.services.ClienteService;

@Component
public class CadastrarClienteConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ClienteService clienteService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-cliente-cliente-cadastrar")
    public void cadastrarCliente(ClienteDto clienteDto) {
        try {
            UsuarioRequestCadastrarDto usuarioRequestCadastrarDto = clienteService.cadastrarCliente(clienteDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-cliente-cadastrado", usuarioRequestCadastrarDto);
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-cliente-cadastrado-erro", new UsuarioRequestResponseDto(clienteDto.getEmail(), e.getMessage()));
        }
    }

    @RabbitListener(queues = "ms-cliente-cliente-cadastrado-compensar")
    public void compensarClienteCadastrado(String email) {
        try {
            clienteService.removerCliente(email);
        } catch (ClienteNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

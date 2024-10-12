package br.com.saga.saga.saga.producers.r01_cadastrar_cliente_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.saga.saga.saga.dtos.ClienteRequestDto;
import br.com.saga.saga.saga.dtos.UsuarioRequestDto;
import br.com.saga.saga.saga.dtos.UsuarioResponseDto;
import jakarta.validation.Valid;

@Component
@RestController
@RequestMapping("/api/saga/cliente")
public class CadastrarClienteUsuarioProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @PostMapping("/cadastrar-cliente")
    public void cadastrarCliente(@RequestBody @Valid ClienteRequestDto clienteRequestDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-cliente-cliente-cadastrar", clienteRequestDto);
    }

    @RabbitListener(queues = "saga-ms-cliente-cliente-cadastrado")
    public void clienteCadastradoListener(UsuarioRequestDto usuarioRequestDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-cliente-cadastrar", usuarioRequestDto);
    }

    @RabbitListener(queues = "saga-ms-cliente-cliente-cadastrado-erro")
    public void clienteCadastradoErroListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-cliente-cliente-cadastrado-compensar", email);
    }

    @RabbitListener(queues = "saga-ms-auth-cliente-cadastrado")
    public void usuarioCadastradoListener(UsuarioResponseDto usuarioResponseDto) {
        System.out.println("Cliente e Usuario criados com sucesso!");
    }

    @RabbitListener(queues = "saga-ms-auth-cliente-cadastrado-erro")
    public void usuarioCadastradoErroListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-cliente-cadastrado-compensar", email);
    }
}

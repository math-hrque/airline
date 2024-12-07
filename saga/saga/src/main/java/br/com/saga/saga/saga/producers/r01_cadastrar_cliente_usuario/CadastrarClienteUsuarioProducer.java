package br.com.saga.saga.saga.producers.r01_cadastrar_cliente_usuario;

import br.com.saga.saga.saga.dtos.UsuarioRequestResponseDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.saga.saga.saga.dtos.UsuarioRequestCadastrarDto;
import br.com.saga.saga.saga.dtos.UsuarioResponseDto;

@Component
public class CadastrarClienteUsuarioProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "saga-ms-cliente-cliente-cadastrado")
    public void clienteCadastradoListener(UsuarioRequestCadastrarDto usuarioRequestCadastrarDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-cliente-cadastrado-endpoint", usuarioRequestCadastrarDto);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-cliente-cadastrar", usuarioRequestCadastrarDto);
    }

    @RabbitListener(queues = "saga-ms-cliente-cliente-cadastrado-erro")
    public void clienteCadastradoErroListener(UsuarioRequestResponseDto usuarioRequestResponseDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-cliente-cadastrado-endpoint", usuarioRequestResponseDto);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-cliente-cliente-cadastrado-compensar", usuarioRequestResponseDto.getEmail());
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

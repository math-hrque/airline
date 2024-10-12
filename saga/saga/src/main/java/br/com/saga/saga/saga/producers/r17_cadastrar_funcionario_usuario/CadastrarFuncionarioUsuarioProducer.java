package br.com.saga.saga.saga.producers.r17_cadastrar_funcionario_usuario;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.saga.saga.saga.dtos.FuncionarioRequestDto;
import br.com.saga.saga.saga.dtos.UsuarioRequestDto;
import br.com.saga.saga.saga.dtos.UsuarioResponseDto;
import jakarta.validation.Valid;

@Component
@RestController
@RequestMapping("/api/saga/funcionario")
public class CadastrarFuncionarioUsuarioProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @PostMapping("/cadastrar-funcionario")
    public void cadastrarFuncionario(@RequestBody @Valid FuncionarioRequestDto funcionarioRequestDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-funcionario-cadastrar", funcionarioRequestDto);
    }

    @RabbitListener(queues = "saga-ms-funcionario-funcionario-cadastrado")
    public void funcionarioCadastradoListener(UsuarioRequestDto usuarioRequestDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-funcionario-cadastrar", usuarioRequestDto);
    }

    @RabbitListener(queues = "saga-ms-funcionario-funcionario-cadastrado-erro")
    public void funcionarioCadastradoErroListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-funcionario-cadastrado-compensar", email);
    }

    @RabbitListener(queues = "saga-ms-auth-funcionario-cadastrado")
    public void usuarioCadastradoListener(UsuarioResponseDto usuarioResponseDto) {
        System.out.println("Funcionario e Usuario criados com sucesso!");
    }

    @RabbitListener(queues = "saga-ms-auth-funcionario-cadastrado-erro")
    public void usuarioCadastradoErroListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-funcionario-cadastrado-compensar", email);
    }
}

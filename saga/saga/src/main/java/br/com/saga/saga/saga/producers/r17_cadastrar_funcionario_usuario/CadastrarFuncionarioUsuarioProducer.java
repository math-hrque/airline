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
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-cadastrar", funcionarioRequestDto);
    }

    @RabbitListener(queues = "ms-funcionario-cadastrado")
    public void funcionarioCadastradoListener(UsuarioRequestDto usuarioRequestDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-cadastrar", usuarioRequestDto);
    }

    @RabbitListener(queues = "ms-funcionario-cadastro-erro")
    public void funcionarioCadastroErroListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-cadastro-compensar-email", email);
    }

    @RabbitListener(queues = "ms-auth-cadastrado")
    public void usuarioCadastradoListener(UsuarioResponseDto usuarioResponseDto) {
        System.out.println("Funcionário e Usuário criados com sucesso!");
    }

    @RabbitListener(queues = "ms-auth-cadastro-erro")
    public void usuarioCadastroErroListener(String email) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-auth-cadastro-compensar-email", email);
    }
}

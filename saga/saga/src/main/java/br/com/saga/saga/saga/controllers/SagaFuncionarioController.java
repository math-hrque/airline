package br.com.saga.saga.saga.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.saga.saga.saga.dtos.FuncionarioRequestDto;
import jakarta.validation.Valid;

@Component
@RestController
@RequestMapping("/saga/ms-funcionario")
@CrossOrigin(origins = "http://localhost:4200")
public class SagaFuncionarioController {

    private static final Logger logger = LoggerFactory.getLogger(SagaFuncionarioController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @PostMapping("/cadastrar-funcionario")
    public ResponseEntity<Object> cadastrarFuncionario(@RequestBody @Valid FuncionarioRequestDto funcionarioRequestDto) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-funcionario-cadastrar", funcionarioRequestDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("SAGA: cadastro de funcionario iniciado. Acompanhe o status.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SAGA: cadastro de funcionario com erro ao iniciar o processo: " + e.getMessage());
        }
    }

    @PutMapping("/atualizar-funcionario")
    public ResponseEntity<Object> atualizarFuncionario(@RequestBody @Valid FuncionarioRequestDto funcionarioRequestDto) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-funcionario-atualizar", funcionarioRequestDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("SAGA: atualização de funcionario iniciado. Acompanhe o status.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SAGA: atualização de funcionario com erro ao iniciar o processo: " + e.getMessage());
        }
    }

    @PutMapping("/inativar-funcionario/{email}")
    public ResponseEntity<Object> inativarFuncionario(@PathVariable("email") String email) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-funcionario-funcionario-inativar", email);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("SAGA: inativação de funcionario iniciado. Acompanhe o status.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SAGA: inativação de funcionario com erro ao iniciar o processo: " + e.getMessage());
        }
    }
}

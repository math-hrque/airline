package br.com.saga.saga.saga.controllers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.saga.saga.saga.dtos.ClienteDto;
import jakarta.validation.Valid;

@Component
@RestController
@RequestMapping("/saga/ms-cliente")
public class SagaClienteController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @PostMapping("/cadastrar-cliente")
    public ResponseEntity<Object> cadastrarCliente(@RequestBody @Valid ClienteDto clienteDto) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-cliente-cliente-cadastrar", clienteDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("SAGA: cadastro de cliente iniciado. Acompanhe o status.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SAGA: cadastro de cliente com erro ao iniciar o processo: " + e.getMessage());
        }
    }
}

package br.com.saga.saga.saga.controllers;

import br.com.saga.saga.saga.utils.DirectMessageListenerContainerBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.saga.saga.saga.dtos.ClienteDto;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
@RestController
@RequestMapping("/saga/ms-cliente")
@CrossOrigin(origins = "http://localhost:4200")
public class SagaClienteController {

    private static final Logger logger = LoggerFactory.getLogger(SagaClienteController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConnectionFactory connectionFactory;

    private static final String EXCHANGE_NAME = "saga-exchange";

    private static final long FUTURE_RESPONSE_TIMEOUT = 30;

    @PostMapping("/cadastrar-cliente")
    public ResponseEntity<Object> cadastrarCliente(@RequestBody @Valid ClienteDto clienteDto) {
        CompletableFuture<Map<String, Object>> executionResponseFuture = new CompletableFuture<>();
        DirectMessageListenerContainer container = DirectMessageListenerContainerBuilder.build(connectionFactory, "saga-ms-cliente-cliente-cadastrado-endpoint", executionResponseFuture);
        container.start();

        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-cliente-cliente-cadastrar", clienteDto);

            Map<String, Object> executionResponseJson = executionResponseFuture.get(FUTURE_RESPONSE_TIMEOUT, TimeUnit.SECONDS);
            container.stop();

            String errorMessage = (String) executionResponseJson.get("errorMessage");

            if (errorMessage != null) {
                executionResponseJson.remove("email");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(executionResponseJson);
            }

            return ResponseEntity.status(HttpStatus.CREATED).header("Content-Type", "application/json").body(executionResponseJson);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SAGA: cadastro de cliente com erro ao iniciar o processo: " + e.getMessage());
        }
    }
}

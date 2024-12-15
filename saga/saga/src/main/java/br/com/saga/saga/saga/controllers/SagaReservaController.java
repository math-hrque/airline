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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.saga.saga.saga.dtos.ReservaManterDto;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
@RestController
@RequestMapping("/saga/ms-reserva")
@CrossOrigin(origins = "http://localhost:4200")
public class SagaReservaController {

    private static final Logger logger = LoggerFactory.getLogger(SagaReservaController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConnectionFactory connectionFactory;

    private static final String EXCHANGE_NAME = "saga-exchange";
    private static final long FUTURE_RESPONSE_TIMEOUT = 30;

    @PostMapping("/cadastrar-reserva")
    public ResponseEntity<Object> cadastrarReserva(@RequestBody @Valid ReservaManterDto reservaManterDto) {
        CompletableFuture<Map<String, Object>> executionResponseFuture = new CompletableFuture<>();
        DirectMessageListenerContainer container = DirectMessageListenerContainerBuilder.build(connectionFactory, "saga-ms-reserva-reserva-cadastrada-endpoint", executionResponseFuture);
        container.start();

        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cadastrar", reservaManterDto);
            
            Map<String, Object> executionResponseJson = executionResponseFuture.get(FUTURE_RESPONSE_TIMEOUT, TimeUnit.SECONDS);
            container.stop();

            String errorMessage = (String) executionResponseJson.get("errorMessage");

            if (errorMessage != null) {
                executionResponseJson.remove("reservaManterDto");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json").body(executionResponseJson);
            }

            return ResponseEntity.status(HttpStatus.CREATED).header("Content-Type", "application/json").body(executionResponseJson);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SAGA: cadastro de reserva com erro: " + e.getMessage());
        }
    }

    @PutMapping("/cancelar-reserva/{codigoReserva}")
    public ResponseEntity<Object> cancelarReserva(@PathVariable("codigoReserva") String codigoReserva) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cancelar", codigoReserva);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("SAGA: cancelamento de reserva iniciado. Acompanhe o status.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SAGA: cancelamento de reserva com erro ao iniciar o processo: " + e.getMessage());
        }
    }
}

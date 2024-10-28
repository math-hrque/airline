package br.com.saga.saga.saga.controllers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.saga.saga.saga.dtos.ReservaManterDto;
import jakarta.validation.Valid;

@Component
@RestController
@RequestMapping("/saga/ms-reserva")
public class SagaReservaController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @PostMapping("/cadastrar-reserva")
    public ResponseEntity<Object> cadastrarReserva(@RequestBody @Valid ReservaManterDto reservaManterDto) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cadastrar", reservaManterDto);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("SAGA: cadastro de reserva iniciado. Acompanhe o status.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SAGA: cadastro de reserva com erro ao iniciar o processo: " + e.getMessage());
        }
    }

    @PutMapping("/cancelar-reserva/{codigoReserva}")
    public ResponseEntity<Object> cancelarReserva(@PathVariable("codigoReserva") String codigoReserva) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cancelar", codigoReserva);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("SAGA: cancelamento de reserva iniciado. Acompanhe o status.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SAGA: cancelamento de reserva com erro ao iniciar o processo: " + e.getMessage());
        }
    }
}

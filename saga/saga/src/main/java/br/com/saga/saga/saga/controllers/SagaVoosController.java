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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping("/saga/ms-voos")
@CrossOrigin(origins = "http://localhost:4200")
public class SagaVoosController {

    private static final Logger logger = LoggerFactory.getLogger(SagaVoosController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @PutMapping("/realizar-voo/{codigoVoo}")
    public ResponseEntity<Object> realizarVoo(@PathVariable("codigoVoo") String codigoVoo) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-voos-voo-realizar", codigoVoo);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("SAGA: realização de voo iniciado. Acompanhe o status.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SAGA: realização de voo com erro ao iniciar o processo: " + e.getMessage());
        }
    }

    @PutMapping("/cancelar-voo/{codigoVoo}")
    public ResponseEntity<Object> cancelarVoo(@PathVariable("codigoVoo") String codigoVoo) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-voos-voo-cancelar", codigoVoo);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("SAGA: cancelamento de voo iniciado. Acompanhe o status.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SAGA: cancelamento de voo com erro ao iniciar o processo: " + e.getMessage());
        }
    }
}

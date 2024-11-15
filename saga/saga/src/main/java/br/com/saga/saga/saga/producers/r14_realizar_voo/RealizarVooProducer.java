package br.com.saga.saga.saga.producers.r14_realizar_voo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.saga.saga.saga.dtos.VooManterDto;

@Component
public class RealizarVooProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "saga-ms-voos-voo-realizado")
    public void vooRealizadoListener(VooManterDto vooManterDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reservas-realizar-voo", vooManterDto);
    }

    @RabbitListener(queues = "saga-ms-voos-voo-realizado-erro")
    public void vooRealizadoErroListener(String codigoVoo) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-voos-voo-realizado-compensar", codigoVoo);
    }

    @RabbitListener(queues = "saga-ms-reserva-reservas-realizadas")
    public void reservasRealizadasListener(VooManterDto vooManterDto) {
        System.out.println("Voo e Reservas realizadas com sucesso!");
    }

    @RabbitListener(queues = "saga-ms-reserva-reservas-realizadas-erro")
    public void reservasRealizadasErroListener(VooManterDto vooManterDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reservas-realizadas-voo-compensar", vooManterDto);
    }
}

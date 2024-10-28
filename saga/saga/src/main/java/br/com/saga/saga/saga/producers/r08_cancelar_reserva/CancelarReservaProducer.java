package br.com.saga.saga.saga.producers.r08_cancelar_reserva;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.saga.saga.saga.dtos.ReservaManterDto;

@Component
public class CancelarReservaProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "saga-ms-reserva-reserva-cancelada")
    public void reservaCanceladaListener(ReservaManterDto reservaManterDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-voos-voo-poltrona-desocupar", reservaManterDto);
    }

    @RabbitListener(queues = "saga-ms-reserva-reserva-cancelada-erro")
    public void reservaCanceladaErroListener(String codigoReserva) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cancelada-compensar", codigoReserva);
    }

    @RabbitListener(queues = "saga-ms-voos-voo-poltrona-desocupada")
    public void vooPoltronaDesocupadaListener(ReservaManterDto reservaManterDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-cliente-milhas-reserva-cancelar", reservaManterDto);
    }

    @RabbitListener(queues = "saga-ms-voos-voo-poltrona-desocupada-erro")
    public void vooPoltronaDesocupadaErroListener(ReservaManterDto reservaManterDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-voos-voo-poltrona-desocupada-compensar", reservaManterDto);
    }

    @RabbitListener(queues = "saga-ms-cliente-milhas-reserva-cancelada")
    public void milhasReservaCanceladaListener(ReservaManterDto reservaManterDto) {
        System.out.println("Reserva cancelada com sucesso!");
    }

    @RabbitListener(queues = "saga-ms-cliente-milhas-reserva-cancelada-erro")
    public void milhasReservaCanceladaErroListener(ReservaManterDto reservaManterDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-cliente-milhas-reserva-cancelada-compensar", reservaManterDto);
    }
}

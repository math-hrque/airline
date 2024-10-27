package br.com.reserva.reserva.consumers.r10_fazer_checkin;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.reserva.reserva.exeptions.ReservaNaoExisteException;
import br.com.reserva.reserva.models.conta_cud.ReservaCUD;
import br.com.reserva.reserva.services.conta_r.ReservaRService;

@Component
public class FazerCheckinConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ReservaRService reservaRService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-reserva-fazer-checkin-contaR")
    public void checkinReserva(ReservaCUD reservaCUD) {
        try {
            reservaRService.checkinReservaR(reservaCUD);
        } catch (ReservaNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

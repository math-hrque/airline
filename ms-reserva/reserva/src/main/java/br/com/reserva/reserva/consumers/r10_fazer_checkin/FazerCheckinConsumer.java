package br.com.reserva.reserva.consumers.r10_fazer_checkin;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.reserva.reserva.exeptions.ReservaNaoExisteException;
import br.com.reserva.reserva.models.conta_cud.ReservaCUD;
import br.com.reserva.reserva.services.conta_cud.ReservaCUDService;
import br.com.reserva.reserva.services.conta_r.ReservaRService;

@Component
public class FazerCheckinConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ReservaRService reservaRService;

    @Autowired
    ReservaCUDService reservaCUDService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-reserva-reserva-fazer-checkin-contaR")
    public void reservaRFazerCheckin(ReservaCUD reservaCUD) {
        try {
            reservaRService.reservaRFazerCheckin(reservaCUD);
        } catch (ReservaNaoExisteException e) {

        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-feito-checkin-contaR-compensar", reservaCUD.getCodigoReserva());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-feito-checkin-contaCUD-compensar", reservaCUD);
        }
    }

    @RabbitListener(queues = "ms-reserva-reserva-feito-checkin-contaR-compensar")
    public void reservaRCheckinFeitoCompensar(String codigoReserva) {
        try {
            reservaRService.reservaRCompensar(codigoReserva);
        } catch (ReservaNaoExisteException e) {

        } catch (Exception e) {

        }
    }

    @RabbitListener(queues = "ms-reserva-reserva-feito-checkin-contaCUD-compensar")
    public void reservaCUDCheckinFeitoCompensar(ReservaCUD reservaCUD) {
        try {
            reservaCUDService.reservaCUDCompensar(reservaCUD);
        } catch (ReservaNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

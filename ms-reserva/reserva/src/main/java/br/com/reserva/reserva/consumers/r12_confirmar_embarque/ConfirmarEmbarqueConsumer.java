package br.com.reserva.reserva.consumers.r12_confirmar_embarque;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.reserva.reserva.exeptions.ReservaNaoExisteException;
import br.com.reserva.reserva.models.conta_cud.ReservaCUD;
import br.com.reserva.reserva.services.conta_cud.ReservaCUDService;
import br.com.reserva.reserva.services.conta_r.ReservaRService;

@Component
public class ConfirmarEmbarqueConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ReservaRService reservaRService;

    @Autowired
    ReservaCUDService reservaCUDService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-reserva-reserva-confirmar-embarque-contaR")
    public void reservaRConfirmarEmbarque(ReservaCUD reservaCUD) {
        try {
            reservaRService.reservaRConfirmarEmbarque(reservaCUD);
        } catch (ReservaNaoExisteException e) {

        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-confirmado-embarque-contaR-compensar", reservaCUD.getCodigoReserva());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-confirmado-embarque-contaCUD-compensar", reservaCUD);
        }
    }

    @RabbitListener(queues = "ms-reserva-reserva-confirmado-embarque-contaR-compensar")
    public void compensarReservaRConfirmadoEmbarqueCompensar(String codigoReserva) {
        try {
            reservaRService.reservaRCompensar(codigoReserva);
        } catch (ReservaNaoExisteException e) {

        } catch (Exception e) {

        }
    }

    @RabbitListener(queues = "ms-reserva-reserva-confirmado-embarque-contaCUD-compensar")
    public void compensarReservaCUDConfirmadoEmbarqueCompensar(ReservaCUD reservaCUD) {
        try {
            reservaCUDService.reservaCUDCompensar(reservaCUD);
        } catch (ReservaNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

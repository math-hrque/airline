package br.com.reserva.reserva.consumers.r08_cancelar_reserva;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.reserva.reserva.dtos.ReservaManterDto;
import br.com.reserva.reserva.dtos.VooManterDto;
import br.com.reserva.reserva.models.conta_cud.ReservaCUD;
import br.com.reserva.reserva.services.conta_cud.ReservaCUDService;
import br.com.reserva.reserva.services.conta_r.ReservaRService;

@Component
public class CancelarReservaConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ReservaCUDService reservaCUDService;

    @Autowired
    ReservaRService reservaRService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-reserva-reserva-cancelar")
    public void cancelarReservaCUD(String codigoReserva) {
        // try {
        //     ReservaManterDto reservaManterCanceladaDto = reservaCUDService.cancelarReservaCUD(codigoReserva);
        //     rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cancelada", reservaManterCanceladaDto);
        // } catch (Exception e) {
        //     rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cancelada-erro", codigoReserva);
        // }
    }

    @RabbitListener(queues = "ms-reserva-reserva-cancelada-compensar")
    public void compensarReservaCUDCancelada(String codigoReserva) {
        // try {
        //     reservaCUDService.reverterReservaCUDCancelada(codigoReserva);
        // } catch (Exception e) {

        // }
    }

    @RabbitListener(queues = "ms-reserva-reserva-cancelada-contaR")
    public void cancelarReservaR(ReservaCUD reservaCUD) {
        // try {
        //     reservaRService.cancelarReservaR(reservaCUD);
        // } catch (Exception e) {

        // }
    }
}

package br.com.reserva.reserva.consumers.r08_cancelar_reserva;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.reserva.reserva.dtos.ReservaManterDto;
import br.com.reserva.reserva.exeptions.MudancaEstadoReservaInvalidaException;
import br.com.reserva.reserva.exeptions.ReservaNaoExisteException;
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
    public void reservaCUDCancelar(String codigoReserva) {
        try {
            ReservaManterDto reservaManterCanceladaDto = reservaCUDService.reservaCUDCancelar(codigoReserva);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cancelada", reservaManterCanceladaDto);
        } catch (ReservaNaoExisteException e) {
        
        } catch (MudancaEstadoReservaInvalidaException e) {
        
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cancelada-contaR-compensar", codigoReserva);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cancelada-erro", codigoReserva);
        }
    }

    @RabbitListener(queues = "ms-reserva-reserva-cancelada-compensar")
    public void compensarReservaCUDCancelada(String codigoReserva) {
        try {
            reservaCUDService.reverterReservaCUDCancelada(codigoReserva);
        } catch (ReservaNaoExisteException e) {

        } catch (Exception e) {

        }
    }

    @RabbitListener(queues = "ms-reserva-reserva-cancelada-contaR")
    public void reservaRCancelar(ReservaCUD reservaCUD) {
        try {
            reservaRService.reservaRCancelar(reservaCUD);
        } catch (ReservaNaoExisteException e) {

        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cancelada-contaR-compensar", reservaCUD.getCodigoReserva());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cancelada-erro", reservaCUD.getCodigoReserva());
        }
    }

    @RabbitListener(queues = "ms-reserva-reserva-cancelada-contaR-compensar")
    public void reservaRCanceladaCompensar(String codigoReserva) {
        try {
            reservaRService.reservaRCompensar(codigoReserva);
        } catch (ReservaNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

package br.com.reserva.reserva.consumers.r12_confirmar_embarque;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.reserva.reserva.exeptions.ReservaNaoExisteException;
import br.com.reserva.reserva.models.conta_cud.ReservaCUD;
import br.com.reserva.reserva.services.conta_r.ReservaRService;

@Component
public class ConfirmarEmbarqueConsumer {

    @Autowired
    ReservaRService reservaRService;

    @RabbitListener(queues = "ms-reserva-reserva-confirmar-embarque-contaR")
    public void reservaRConfirmarEmbarque(ReservaCUD reservaCUD) {
        try {
            reservaRService.reservaRConfirmarEmbarque(reservaCUD);
        } catch (ReservaNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

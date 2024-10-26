package br.com.reserva.reserva.consumers.r12_confirmar_embarque;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.reserva.reserva.exeptions.ReservaNaoExisteException;
import br.com.reserva.reserva.models.conta_cud.ReservaCUD;
import br.com.reserva.reserva.services.conta_r.ReservaRService;

@Component
public class ConfirmarEmbarqueConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ReservaRService reservaRService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-reserva-confirmar-embarque-contaR")
    public void realizarReservas(ReservaCUD reservaCUD) {
        try {
            reservaRService.confirmarEmbarqueR(reservaCUD);
        } catch (ReservaNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}
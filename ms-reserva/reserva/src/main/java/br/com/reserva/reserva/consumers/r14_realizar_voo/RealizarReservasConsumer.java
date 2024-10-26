package br.com.reserva.reserva.consumers.r14_realizar_voo;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.reserva.reserva.dtos.VooManterDto;
import br.com.reserva.reserva.exeptions.ReservaNaoExisteException;
import br.com.reserva.reserva.models.conta_cud.ReservaCUD;
import br.com.reserva.reserva.services.conta_cud.ReservaCUDService;
import br.com.reserva.reserva.services.conta_r.ReservaRService;

@Component
public class RealizarReservasConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ReservaCUDService reservaCUDService;

    @Autowired
    ReservaRService reservaRService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-reserva-reservas-realizar")
    public void realizarReservasCUD(VooManterDto vooManterDto) {
        try {
            reservaCUDService.realizarReservasCUD(vooManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reservas-realizadas", vooManterDto);
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reservas-realizadas-erro", vooManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-realizado-erro", vooManterDto.getCodigoVoo());
        }
    }

    @RabbitListener(queues = "ms-reserva-reservas-realizadas-compensar")
    public void compensarReservasRealizadasCUD(VooManterDto vooManterDto) {
        try {
            reservaCUDService.reverterReservasRealizadasCUD(vooManterDto);
        } catch (Exception e) {

        }
    }

    @RabbitListener(queues = "ms-reserva-reservas-realizadas-contaR")
    public void realizarReservas(List<ReservaCUD> listaReservaCUD) {
        try {
            reservaRService.realizarReservasR(listaReservaCUD);
        } catch (Exception e) {

        }
    }
}

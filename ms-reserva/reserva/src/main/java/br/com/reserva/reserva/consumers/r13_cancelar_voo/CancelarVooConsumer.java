package br.com.reserva.reserva.consumers.r13_cancelar_voo;

import java.util.List;
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
public class CancelarVooConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ReservaCUDService reservaCUDService;

    @Autowired
    ReservaRService reservaRService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-reserva-reservas-cancelar-voo")
    public void cancelarReservasCUDVoo(VooManterDto vooManterDto) {
        // try {
        //     List<ReservaManterDto> listaReservaManterCanceladasDto = reservaCUDService.cancelarReservasCUDVoo(vooManterDto);
        //     rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reservas-canceladas-voo", listaReservaManterCanceladasDto);
        // } catch (Exception e) {
        //     rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reservas-canceladas-voo-erro", vooManterDto.getCodigoVoo());
        //     rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-cancelado-erro", vooManterDto.getCodigoVoo());
        // }
    }

    @RabbitListener(queues = "ms-reserva-reservas-canceladas-voo-compensar")
    public void compensarReservasCUDCanceladasVoo(String codigoVoo) {
        // try {
        //     reservaCUDService.reverterReservasCUDCanceladasVoo(codigoVoo);
        // } catch (Exception e) {

        // }
    }

    @RabbitListener(queues = "ms-reserva-reservas-canceladas-voo-contaR")
    public void cancelarReservasRVoo(List<ReservaCUD> listaReservaCUD) {
        // try {
        //     reservaRService.cancelarReservasRVoo(listaReservaCUD);
        // } catch (Exception e) {

        // }
    }
}

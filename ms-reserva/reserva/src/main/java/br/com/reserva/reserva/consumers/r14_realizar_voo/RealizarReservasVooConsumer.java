package br.com.reserva.reserva.consumers.r14_realizar_voo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.reserva.reserva.dtos.VooManterDto;
import br.com.reserva.reserva.services.conta_cud.ReservaCUDService;
import br.com.reserva.reserva.services.conta_r.ReservaRService;

@Component
public class RealizarReservasVooConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ReservaCUDService reservaCUDService;

    @Autowired
    ReservaRService reservaRService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-reserva-reservas-realizar-voo")
    public void realizarReservasCUDVoo(VooManterDto vooManterDto) {
        try {
            reservaCUDService.realizarReservasCUDVoo(vooManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reservas-realizadas", vooManterDto);
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reservas-realizadas-voo-contaR-compensar", vooManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reservas-realizadas-erro", vooManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-realizado-erro", vooManterDto.getCodigoVoo());
        }
    }

    @RabbitListener(queues = "ms-reserva-reservas-realizadas-voo-compensar")
    public void reverterReservasCUDRealizadasVoo(VooManterDto vooManterDto) {
        try {
            reservaCUDService.reverterReservasCUDRealizadasVoo(vooManterDto);
        } catch (Exception e) {

        }
    }

    @RabbitListener(queues = "ms-reserva-reservas-realizadas-voo-contaR")
    public void reservasRVooRealizar(VooManterDto vooManterDto) {
        try {
            reservaRService.reservasRVooRealizar(vooManterDto);
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reservas-realizadas-voo-contaR-compensar", vooManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reservas-realizadas-erro", vooManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-realizado-erro", vooManterDto.getCodigoVoo());
        }
    }

    @RabbitListener(queues = "ms-reserva-reservas-realizadas-voo-contaR-compensar")
    public void reservasRVooRealizadoCompensar(VooManterDto vooManterDto) {
        try {
            reservaRService.reservasRVooCompensar(vooManterDto);
        } catch (Exception e) {

        }
    }
}

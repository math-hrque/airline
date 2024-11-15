package br.com.cliente.cliente.consumers.r08_cancelar_reserva;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cliente.cliente.dtos.ReservaManterDto;
import br.com.cliente.cliente.exeptions.ClienteNaoExisteException;
import br.com.cliente.cliente.services.ClienteService;

@Component
public class CancelarReservaConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ClienteService clienteService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-cliente-milhas-reserva-cancelar")
    public void milhasReservaCancelar(ReservaManterDto reservaManterDto) {
        try {
            clienteService.milhasReservaCancelar(reservaManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-milhas-reserva-cancelada", reservaManterDto);
        } catch (ClienteNaoExisteException e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cancelada-contaR-compensar", reservaManterDto.getCodigoReserva());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cancelada-erro", reservaManterDto.getCodigoReserva());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-poltrona-desocupada-erro", reservaManterDto);
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cancelada-contaR-compensar", reservaManterDto.getCodigoReserva());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cancelada-erro", reservaManterDto.getCodigoReserva());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-poltrona-desocupada-erro", reservaManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-milhas-reserva-cancelada-erro", reservaManterDto);
        }
    }

    @RabbitListener(queues = "ms-cliente-milhas-reserva-cancelada-compensar")
    public void compensarMilhasReservaCancelada(ReservaManterDto reservaManterDto) {
        try {
            clienteService.reverterMilhasReservaCancelada(reservaManterDto);
        } catch (ClienteNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

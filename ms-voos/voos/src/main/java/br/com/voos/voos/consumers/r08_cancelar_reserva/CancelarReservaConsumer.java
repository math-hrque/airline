package br.com.voos.voos.consumers.r08_cancelar_reserva;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.voos.voos.dtos.ReservaManterDto;
import br.com.voos.voos.exeptions.VooNaoExisteException;
import br.com.voos.voos.services.VoosService;

@Component
public class CancelarReservaConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    VoosService voosService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-voos-voo-poltrona-desocupar")
    public void desocuparPoltronaVoo(ReservaManterDto reservaManterDto) {
        try {
            ReservaManterDto ReservaManterVooDto = voosService.desocuparPoltronaVoo(reservaManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-poltrona-desocupada", ReservaManterVooDto);
        } catch (VooNaoExisteException e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cancelada-contaR-compensar", reservaManterDto.getCodigoReserva());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cancelada-erro", reservaManterDto.getCodigoReserva());
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cancelada-contaR-compensar", reservaManterDto.getCodigoReserva());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cancelada-erro", reservaManterDto.getCodigoReserva());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-poltrona-desocupada-erro", reservaManterDto);
        }
    }

    @RabbitListener(queues = "ms-voos-voo-poltrona-desocupada-compensar")
    public void compensarVooPoltronaDesocupado(ReservaManterDto reservaManterDto) {
        try {
            voosService.reverterVooPoltronaDesocupado(reservaManterDto);
        } catch (VooNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

package br.com.voos.voos.consumers.r07_efetuar_reserva;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.voos.voos.dtos.ReservaManterDto;
import br.com.voos.voos.exeptions.LimitePoltronasOcupadasVooException;
import br.com.voos.voos.exeptions.VooNaoExisteException;
import br.com.voos.voos.services.VoosService;

@Component
public class EfetuarReservaConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    VoosService voosService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-voos-voo-poltrona-ocupar")
    public void ocuparPoltronaVoo(ReservaManterDto reservaManterDto) {
        try {
            ReservaManterDto ReservaManterVooDto = voosService.ocuparPoltronaVoo(reservaManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-poltrona-ocupada", ReservaManterVooDto);
        } catch (VooNaoExisteException e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cadastrada-erro", reservaManterDto);
        } catch (LimitePoltronasOcupadasVooException e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cadastrada-erro", reservaManterDto);
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cadastrada-erro", reservaManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-poltrona-ocupada-erro", reservaManterDto);
        }
    }

    @RabbitListener(queues = "ms-voos-voo-poltrona-ocupada-compensar")
    public void compensarVooPoltronaOcupada(ReservaManterDto reservaManterDto) {
        try {
            voosService.reverterVooPoltronaOcupada(reservaManterDto);
        } catch (VooNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

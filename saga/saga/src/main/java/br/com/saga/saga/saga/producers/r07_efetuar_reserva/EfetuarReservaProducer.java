package br.com.saga.saga.saga.producers.r07_efetuar_reserva;

import br.com.saga.saga.saga.dtos.ReservaManterErroDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.saga.saga.saga.dtos.ReservaManterDto;

@Component
public class EfetuarReservaProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "saga-ms-reserva-reserva-cadastrada")
    public void reservaCadastradaListener(ReservaManterDto reservaManterDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-voos-voo-poltrona-ocupar", reservaManterDto);
    }

    @RabbitListener(queues = "saga-ms-reserva-reserva-cadastrada-erro")
    public void reservaCadastradaErroListener(ReservaManterErroDto reservaManterErroDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cadastrada-endpoint", reservaManterErroDto);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cadastrada-compensar", reservaManterErroDto.getReservaManterDto());
    }

    @RabbitListener(queues = "saga-ms-voos-voo-poltrona-ocupada")
    public void vooPoltronaOcupadaListener(ReservaManterDto reservaManterDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-cliente-milhas-reserva-cadastrar", reservaManterDto);
    }

    @RabbitListener(queues = "saga-ms-voos-voo-poltrona-ocupada-erro")
    public void vooPoltronaOcupadaErroListener(ReservaManterDto reservaManterDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-voos-voo-poltrona-ocupada-compensar", reservaManterDto);
    }

    @RabbitListener(queues = "saga-ms-cliente-milhas-reserva-cadastrada")
    public void milhasReservaCadastradaListener(ReservaManterDto reservaManterDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cadastrada-endpoint", reservaManterDto);
        System.out.println("Reserva criada com sucesso!");
    }

    @RabbitListener(queues = "saga-ms-cliente-milhas-reserva-cadastrada-erro")
    public void milhasReservaCadastradaErroListener(ReservaManterErroDto reservaManterErroDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cadastrada-endpoint", reservaManterErroDto);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-cliente-milhas-reserva-cadastrada-compensar", reservaManterErroDto.getReservaManterDto());
    }
}

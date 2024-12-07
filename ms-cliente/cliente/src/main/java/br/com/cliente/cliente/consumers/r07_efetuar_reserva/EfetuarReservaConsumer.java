package br.com.cliente.cliente.consumers.r07_efetuar_reserva;

import br.com.cliente.cliente.dtos.ReservaManterErroDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cliente.cliente.dtos.ReservaManterDto;
import br.com.cliente.cliente.exeptions.ClienteNaoExisteException;
import br.com.cliente.cliente.exeptions.SemSaldoMilhasSuficientesClienteException;
import br.com.cliente.cliente.services.ClienteService;

@Component
public class EfetuarReservaConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ClienteService clienteService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-cliente-milhas-reserva-cadastrar")
    public void milhasReservaCadastrar(ReservaManterDto reservaManterDto) {
        try {
            clienteService.milhasReservaCadastrar(reservaManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-milhas-reserva-cadastrada", reservaManterDto);
        } catch (ClienteNaoExisteException | SemSaldoMilhasSuficientesClienteException e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cadastrada-erro", new ReservaManterErroDto(reservaManterDto, e.getMessage()));
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cadastrada-contaR-compensar", reservaManterDto.getCodigoReserva());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-poltrona-ocupada-erro", reservaManterDto);
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reserva-cadastrada-erro", new ReservaManterErroDto(reservaManterDto, e.getMessage()));
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cadastrada-contaR-compensar", reservaManterDto.getCodigoReserva());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-poltrona-ocupada-erro", reservaManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-milhas-reserva-cadastrada-erro", reservaManterDto);
        }
    }

    @RabbitListener(queues = "ms-cliente-milhas-reserva-cadastrada-compensar")
    public void compensarMilhasReservaCadastrada(ReservaManterDto reservaManterDto) {
        try {
            clienteService.reverterMilhasReservaCadastrada(reservaManterDto);
        } catch (ClienteNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

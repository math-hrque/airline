package br.com.cliente.cliente.consumers.r13_cancelar_voo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cliente.cliente.dtos.ReservaManterDto;
import br.com.cliente.cliente.exeptions.ClienteNaoExisteException;
import br.com.cliente.cliente.services.ClienteService;

import java.util.List;

@Component
public class CancelarVooConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ClienteService clienteService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-cliente-milhas-reservas-cancelar-voo")
    public void milhasReservasCancelarVoo(List<ReservaManterDto> listaReservaManterDto) {
        try {
            clienteService.milhasReservasCancelarVoo(listaReservaManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-milhas-reservas-canceladas-voo", listaReservaManterDto);
        } catch (ClienteNaoExisteException e) {

        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-milhas-reservas-canceladas-voo-erro", listaReservaManterDto);
        }
    }

    @RabbitListener(queues = "ms-cliente-milhas-reservas-canceladas-voo-compensar")
    public void compensarMilhasReservasCanceladasVoo(List<ReservaManterDto> listaReservaManterDto) {
        try {
            clienteService.reverterMilhasReservasCanceladasVoo(listaReservaManterDto);
        } catch (ClienteNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

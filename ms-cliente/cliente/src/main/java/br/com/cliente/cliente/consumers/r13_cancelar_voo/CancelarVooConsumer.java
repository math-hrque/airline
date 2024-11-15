package br.com.cliente.cliente.consumers.r13_cancelar_voo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.cliente.cliente.dtos.ReservaManterDto;
import br.com.cliente.cliente.dtos.VooManterDto;
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
        } catch (Exception e) {
            VooManterDto vooManterDto = new VooManterDto();
            if (!listaReservaManterDto.isEmpty()) {
                vooManterDto.setCodigoVoo(listaReservaManterDto.get(0).getCodigoVoo());
            }
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-cliente-milhas-reservas-canceladas-voo-erro", listaReservaManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reservas-canceladas-voo-contaR-compensar", vooManterDto);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-reserva-reservas-canceladas-voo-erro", vooManterDto.getCodigoVoo());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-cancelado-erro", vooManterDto.getCodigoVoo());
        }
    }

    @RabbitListener(queues = "ms-cliente-milhas-reservas-canceladas-voo-compensar")
    public void compensarMilhasReservasCanceladasVoo(List<ReservaManterDto> listaReservaManterDto) {
        try {
            clienteService.reverterMilhasReservasCanceladasVoo(listaReservaManterDto);
        } catch (Exception e) {

        }
    }
}

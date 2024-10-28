package br.com.saga.saga.saga.producers.r13_cancelar_voo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.saga.saga.saga.dtos.ReservaManterDto;
import br.com.saga.saga.saga.dtos.VooManterDto;

import java.util.List;

@Component
public class CancelarVooProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "saga-ms-voos-voo-cancelado")
    public void vooCanceladoListener(VooManterDto vooManterDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reservas-cancelar-voo", vooManterDto);
    }

    @RabbitListener(queues = "saga-ms-voos-voo-cancelado-erro")
    public void vooCanceladoErroListener(String codigoVoo) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-voos-voo-cancelado-compensar", codigoVoo);
    }

    @RabbitListener(queues = "saga-ms-reserva-reservas-canceladas-voo")
    public void reservasCanceladasVooListener(List<ReservaManterDto> listaReservaManterDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-cliente-milhas-reservas-cancelar-voo", listaReservaManterDto);
    }

    @RabbitListener(queues = "saga-ms-reserva-reservas-canceladas-voo-erro")
    public void reservasCanceladasVooErroListener(String codigoVoo) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reservas-canceladas-voo-compensar", codigoVoo);
    }

    @RabbitListener(queues = "saga-ms-cliente-milhas-reservas-canceladas-voo")
    public void milhasReservasCanceladasVooListener(List<ReservaManterDto> listaReservaManterDto) {
        System.out.println("Voo cancelado com sucesso!");
    }

    @RabbitListener(queues = "saga-ms-cliente-milhas-reservas-canceladas-voo-erro")
    public void milhasReservasCanceladasVooErroListener(List<ReservaManterDto> listaReservaManterDto) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-cliente-milhas-reservas-canceladas-voo-compensar", listaReservaManterDto);
    }
}

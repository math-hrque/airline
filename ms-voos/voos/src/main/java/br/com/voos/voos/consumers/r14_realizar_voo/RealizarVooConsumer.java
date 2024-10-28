package br.com.voos.voos.consumers.r14_realizar_voo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.voos.voos.dtos.VooManterDto;
import br.com.voos.voos.exeptions.MudancaEstadoVooInvalidaException;
import br.com.voos.voos.exeptions.VooNaoExisteException;
import br.com.voos.voos.services.VoosService;

@Component
public class RealizarVooConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    VoosService voosService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-voos-voo-realizar")
    public void realizarVoo(String codigoVoo) {
        try {
            VooManterDto vooManterRealizadoDto = voosService.realizar(codigoVoo);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-realizado", vooManterRealizadoDto);
        } catch (VooNaoExisteException e) {

        } catch (MudancaEstadoVooInvalidaException e) {

        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-realizado-erro", codigoVoo);
        }
    }

    @RabbitListener(queues = "ms-voos-voo-realizado-compensar")
    public void compensarVooRealizado(String codigoVoo) {
        try {
            voosService.reverterRealizado(codigoVoo);
        } catch (VooNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

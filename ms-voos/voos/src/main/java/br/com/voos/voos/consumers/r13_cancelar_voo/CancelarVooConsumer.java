package br.com.voos.voos.consumers.r13_cancelar_voo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.voos.voos.dtos.VooManterDto;
import br.com.voos.voos.exeptions.MudancaEstadoVooInvalidaException;
import br.com.voos.voos.exeptions.VooNaoExisteException;
import br.com.voos.voos.services.VoosService;

@Component
public class CancelarVooConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    VoosService voosService;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @RabbitListener(queues = "ms-voos-voo-cancelar")
    public void cancelarVoo(String codigoVoo) {
        try {
            VooManterDto vooManterCanceladoDto = voosService.cancelar(codigoVoo);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-cancelado", vooManterCanceladoDto);
        } catch (VooNaoExisteException e) {

        } catch (MudancaEstadoVooInvalidaException e) {

        } catch (Exception e) {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "saga-ms-voos-voo-cancelado-erro", codigoVoo);
        }
    }

    @RabbitListener(queues = "ms-voos-voo-cancelado-compensar")
    public void compensarVooCancelado(String codigoVoo) {
        try {
            voosService.reverterCancelado(codigoVoo);
        } catch (VooNaoExisteException e) {

        } catch (Exception e) {

        }
    }
}

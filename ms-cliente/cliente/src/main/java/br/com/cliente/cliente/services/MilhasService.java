package br.com.cliente.cliente.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cliente.cliente.dtos.MilhasDto;
import br.com.cliente.cliente.dtos.ReservaManterDto;
import br.com.cliente.cliente.enums.TipoTransacao;
import br.com.cliente.cliente.models.Cliente;
import br.com.cliente.cliente.models.Milhas;
import br.com.cliente.cliente.repositories.MilhasRepository;
import br.com.cliente.cliente.repositories.TransacaoRepository;


@Service
public class MilhasService {

    @Autowired
    private MilhasRepository milhasRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    public ReservaManterDto milhasReservaCadastrar(ReservaManterDto reservaManterDto, Cliente cliente) {
        Milhas milhasCadastrar = new Milhas();
        milhasCadastrar.setValorReais(reservaManterDto.getMilhasUtilizadas() * 5.00);
        milhasCadastrar.setQuantidadeMilhas(reservaManterDto.getMilhasUtilizadas());
        milhasCadastrar.setDescricao(reservaManterDto.getCodigoAeroportoOrigem() + "->" + reservaManterDto.getCodigoAeroportoDestino());
        milhasCadastrar.setCodigoReserva(reservaManterDto.getCodigoReserva());
        milhasCadastrar.setCliente(cliente);
        milhasCadastrar.setTransacao(transacaoRepository.findByTipoTransacao(TipoTransacao.SAIDA));
        milhasRepository.saveMilhas(milhasCadastrar);
        return reservaManterDto;
    }

    public ReservaManterDto milhasReservaCancelar(ReservaManterDto reservaManterDto, Cliente cliente) {
        Milhas milhasCadastrar = new Milhas();
        milhasCadastrar.setValorReais(reservaManterDto.getMilhasUtilizadas() * 5.00);
        milhasCadastrar.setQuantidadeMilhas(reservaManterDto.getMilhasUtilizadas());
        milhasCadastrar.setDescricao("CANCELADO " + reservaManterDto.getCodigoAeroportoOrigem() + "->" + reservaManterDto.getCodigoAeroportoDestino());
        milhasCadastrar.setCodigoReserva(reservaManterDto.getCodigoReserva());
        milhasCadastrar.setCliente(cliente);
        milhasCadastrar.setTransacao(transacaoRepository.findByTipoTransacao(TipoTransacao.ENTRADA));
        milhasRepository.saveMilhas(milhasCadastrar);
        return reservaManterDto;
    }

    public ReservaManterDto milhasReservaCancelarVoo(ReservaManterDto reservaManterDto, Cliente cliente) {
        Milhas milhasCadastrar = new Milhas();
        milhasCadastrar.setValorReais(reservaManterDto.getMilhasUtilizadas() * 5.00);
        milhasCadastrar.setQuantidadeMilhas(reservaManterDto.getMilhasUtilizadas());
        milhasCadastrar.setDescricao("CANCELADO VOO " + reservaManterDto.getCodigoAeroportoOrigem() + "->" + reservaManterDto.getCodigoAeroportoDestino());
        milhasCadastrar.setCodigoReserva(reservaManterDto.getCodigoReserva());
        milhasCadastrar.setCliente(cliente);
        milhasCadastrar.setTransacao(transacaoRepository.findByTipoTransacao(TipoTransacao.ENTRADA));
        milhasRepository.saveMilhas(milhasCadastrar);
        return reservaManterDto;
    }

    public void comprarMilhas(int quantidadeMilhas, Cliente cliente){
        Milhas milhasComprar = new Milhas();
        milhasComprar.setValorReais(quantidadeMilhas * 5.00);
        milhasComprar.setQuantidadeMilhas(quantidadeMilhas);
        milhasComprar.setDescricao("COMPRA DE MILHAS");
        milhasComprar.setCliente(cliente);
        milhasComprar.setTransacao(transacaoRepository.findByTipoTransacao(TipoTransacao.ENTRADA));
        milhasComprar.setDataTransacao(OffsetDateTime.now());
        milhasRepository.save(milhasComprar);
    }

    public List<MilhasDto> consultarExtratoMilhas(Cliente cliente){
        Optional<List<Milhas>> transacoesMilhas = milhasRepository.findByClienteIdCliente(cliente.getIdCliente());
        
        if(!transacoesMilhas.isPresent()){
            return null;
        }

        return transacoesMilhas.get().stream() 
            .map(milhas -> {
                MilhasDto milhasDTO = new MilhasDto();
                milhasDTO.setIdMilhas(milhas.getIdMilhas());
                milhasDTO.setDataTransacao(milhas.getDataTransacao());
                milhasDTO.setValorReais(milhas.getValorReais());
                milhasDTO.setQuantidadesMilhas(milhas.getQuantidadeMilhas());
                milhasDTO.setDescricao(milhas.getDescricao());
                milhasDTO.setCodigoReserva(milhas.getCodigoReserva());
                milhasDTO.setTipoTransacao(milhas.getTransacao().getTipoTransacao()); 
                return milhasDTO; 
            })
            .collect(Collectors.toList());
    }
}

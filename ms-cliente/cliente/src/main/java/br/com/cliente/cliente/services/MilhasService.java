package br.com.cliente.cliente.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cliente.cliente.dtos.MilhasDto;
import br.com.cliente.cliente.dtos.ReservaManterDto;
import br.com.cliente.cliente.enums.TipoTransacao;
import br.com.cliente.cliente.exeptions.ClienteNaoExisteException;
import br.com.cliente.cliente.models.Cliente;
import br.com.cliente.cliente.models.Milhas;
import br.com.cliente.cliente.repositories.MilhasRepository;
import br.com.cliente.cliente.repositories.TransacaoRepository;


@Service
public class MilhasService {

    @Autowired
    private ModelMapper mapper;

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
        milhasRepository.save(milhasCadastrar);
        return reservaManterDto;
    }

    public ReservaManterDto reverterMilhasReservaCadastrar(ReservaManterDto reservaManterDto, Cliente cliente) {
        Milhas milhasCadastrar = new Milhas();
        milhasCadastrar.setValorReais(reservaManterDto.getMilhasUtilizadas() * 5.00);
        milhasCadastrar.setQuantidadeMilhas(reservaManterDto.getMilhasUtilizadas());
        milhasCadastrar.setDescricao("CANCELADO " + reservaManterDto.getCodigoAeroportoOrigem() + "->" + reservaManterDto.getCodigoAeroportoDestino());
        milhasCadastrar.setCodigoReserva(reservaManterDto.getCodigoReserva());
        milhasCadastrar.setCliente(cliente);
        milhasCadastrar.setTransacao(transacaoRepository.findByTipoTransacao(TipoTransacao.ENTRADA));
        milhasRepository.save(milhasCadastrar);
        return reservaManterDto;
    }

    public void comprarMilhas(MilhasDto milhasDto, Cliente cliente){
        Milhas milhasComprar = new Milhas();
        milhasComprar.setValorReais(milhasDto.getQuantidadesMilhas() * 5.00);
        milhasComprar.setQuantidadeMilhas(milhasDto.getQuantidadesMilhas());
        milhasComprar.setDescricao("COMPRA DE MILHAS");
        milhasComprar.setCliente(cliente);
        milhasComprar.setTransacao(transacaoRepository.findByTipoTransacao(TipoTransacao.ENTRADA));
        milhasComprar.setDataTransacao(milhasDto.getDataTransacao());
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

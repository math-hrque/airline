package br.com.cliente.cliente.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cliente.cliente.dtos.ReservaManterDto;
import br.com.cliente.cliente.enums.TipoTransacao;
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
}

package br.com.reserva.reserva.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.repositories.EstadoReservaRepository;
import br.com.reserva.reserva.repositories.HistoricoAlteracaoEstadoReservaRepository;
import br.com.reserva.reserva.utils.RedisHistoricoAlteracaoEstadoReservaCache;

@Service
public class HistoricoAlteracaoEstadoReservaService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RedisHistoricoAlteracaoEstadoReservaCache redisHistoricoAlteracaoEstadoReservaCache;

    @Autowired
    private HistoricoAlteracaoEstadoReservaRepository historicoAlteracaoEstadoReservaRepository;

    @Autowired
    private EstadoReservaRepository estadoReservaRepository;

}

package br.com.reserva.reserva.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.repositories.EstadoReservaRepository;
import br.com.reserva.reserva.repositories.ReservaRepository;
import br.com.reserva.reserva.utils.RedisReservaCache;

@Service
public class ReservaService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RedisReservaCache redisReservaCache;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EstadoReservaRepository estadoReservaRepository;

    @Autowired
    private HistoricoAlteracaoEstadoReservaService historicoAlteracaoEstadoReservaService;

}

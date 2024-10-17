package br.com.reserva.reserva.services.conta_r;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.repositories.conta_r.ReservaRRepository;
import br.com.reserva.reserva.utils.conta_r.RedisReservaRCache;

@Service
public class ReservaRService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RedisReservaRCache redisReservaRCache;

    @Autowired
    private ReservaRRepository reservaRRepository;

    @Autowired
    private HistoricoAlteracaoEstadoReservaRService historicoAlteracaoEstadoReservaRService;


}

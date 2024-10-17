package br.com.reserva.reserva.services.conta_r;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.repositories.conta_r.HistoricoAlteracaoEstadoReservaRRepository;
import br.com.reserva.reserva.utils.conta_r.RedisHistoricoAlteracaoEstadoReservaRCache;

@Service
public class HistoricoAlteracaoEstadoReservaRService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RedisHistoricoAlteracaoEstadoReservaRCache redisHistoricoAlteracaoEstadoReservaRCache;

    @Autowired
    private HistoricoAlteracaoEstadoReservaRRepository historicoAlteracaoEstadoReservaRRepository;


}

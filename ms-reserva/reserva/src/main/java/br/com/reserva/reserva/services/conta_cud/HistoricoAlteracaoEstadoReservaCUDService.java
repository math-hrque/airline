package br.com.reserva.reserva.services.conta_cud;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.repositories.conta_cud.EstadoReservaCUDRepository;
import br.com.reserva.reserva.repositories.conta_cud.HistoricoAlteracaoEstadoReservaCUDRepository;
import br.com.reserva.reserva.utils.conta_cud.RedisHistoricoAlteracaoEstadoReservaCUDCache;

@Service
public class HistoricoAlteracaoEstadoReservaCUDService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RedisHistoricoAlteracaoEstadoReservaCUDCache redisHistoricoAlteracaoEstadoReservaCUDCache;

    @Autowired
    private HistoricoAlteracaoEstadoReservaCUDRepository historicoAlteracaoEstadoReservaCUDRepository;

    @Autowired
    private EstadoReservaCUDRepository estadoReservaCUDRepository;

    
}

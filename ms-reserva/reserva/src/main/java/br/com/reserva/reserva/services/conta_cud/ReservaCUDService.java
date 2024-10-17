package br.com.reserva.reserva.services.conta_cud;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.repositories.conta_cud.EstadoReservaCUDRepository;
import br.com.reserva.reserva.repositories.conta_cud.ReservaCUDRepository;
import br.com.reserva.reserva.utils.conta_cud.RedisReservaCUDCache;

@Service
public class ReservaCUDService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RedisReservaCUDCache redisReservaCUDCache;

    @Autowired
    private ReservaCUDRepository reservaCUDRepository;

    @Autowired
    private EstadoReservaCUDRepository estadoReservaCUDRepository;

    @Autowired
    private HistoricoAlteracaoEstadoReservaCUDService historicoAlteracaoEstadoReservaCUDService;

    
}

package br.com.reserva.reserva.services.conta_cud;

import java.util.List;
import java.util.Optional;
import java.time.OffsetDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.enums.TipoEstadoReserva;
import br.com.reserva.reserva.models.conta_cud.EstadoReservaCUD;
import br.com.reserva.reserva.models.conta_cud.HistoricoAlteracaoEstadoReservaCUD;
import br.com.reserva.reserva.models.conta_cud.ReservaCUD;
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

    public HistoricoAlteracaoEstadoReservaCUD alteraHistoricoEstadoReserva(ReservaCUD reservaCUD, TipoEstadoReserva novoTipoEstadoReserva) {
        Optional<HistoricoAlteracaoEstadoReservaCUD> historicoAlteracaoEstadoReservaCUDBD = historicoAlteracaoEstadoReservaCUDRepository.findByReservaCodigoReserva(reservaCUD.getCodigoReserva());
        HistoricoAlteracaoEstadoReservaCUD historicoAlteracaoEstadoReservaCUD = new HistoricoAlteracaoEstadoReservaCUD();
        if (historicoAlteracaoEstadoReservaCUDBD.isPresent()) {
            HistoricoAlteracaoEstadoReservaCUD historicoAlteracaoEstadoReservaCUDCache = redisHistoricoAlteracaoEstadoReservaCUDCache.getCache(historicoAlteracaoEstadoReservaCUD.getReserva().getCodigoReserva());
            if (historicoAlteracaoEstadoReservaCUDCache == null) {
                redisHistoricoAlteracaoEstadoReservaCUDCache.saveCache(historicoAlteracaoEstadoReservaCUD);
            }
            historicoAlteracaoEstadoReservaCUD = historicoAlteracaoEstadoReservaCUDBD.get();
            EstadoReservaCUD estadoReservaOrigemCUD = estadoReservaCUDRepository.findByTipoEstadoReserva(historicoAlteracaoEstadoReservaCUDBD.get().getEstadoReservaDestino().getTipoEstadoReserva());
            historicoAlteracaoEstadoReservaCUD.setEstadoReservaOrigem(estadoReservaOrigemCUD);
            EstadoReservaCUD estadoReservaDestinoCUD = estadoReservaCUDRepository.findByTipoEstadoReserva(novoTipoEstadoReserva);
            historicoAlteracaoEstadoReservaCUD.setEstadoReservaDestino(estadoReservaDestinoCUD);
            historicoAlteracaoEstadoReservaCUD.setDataAlteracaoEstadoReserva(OffsetDateTime.now());
        } else {
            historicoAlteracaoEstadoReservaCUD.setReserva(reservaCUD);
            EstadoReservaCUD estadoReservaOrigemCUD = estadoReservaCUDRepository.findByTipoEstadoReserva(reservaCUD.getEstadoReserva().getTipoEstadoReserva());
            historicoAlteracaoEstadoReservaCUD.setEstadoReservaOrigem(estadoReservaOrigemCUD);
            EstadoReservaCUD estadoReservaDestinoCUD = estadoReservaCUDRepository.findByTipoEstadoReserva(novoTipoEstadoReserva);
            historicoAlteracaoEstadoReservaCUD.setEstadoReservaDestino(estadoReservaDestinoCUD);
            historicoAlteracaoEstadoReservaCUD.setDataAlteracaoEstadoReserva(OffsetDateTime.now());
        }

        historicoAlteracaoEstadoReservaCUDRepository.save(historicoAlteracaoEstadoReservaCUD);
        return historicoAlteracaoEstadoReservaCUD;
    }


    public void reverterHistoricoEstadoReserva(List<ReservaCUD> listaReservaCUD) {
        for (ReservaCUD reservaCUD : listaReservaCUD) {
            HistoricoAlteracaoEstadoReservaCUD historicoAlteracaoEstadoReservaCUDCache = redisHistoricoAlteracaoEstadoReservaCUDCache.getCache(reservaCUD.getCodigoVoo());
            if (historicoAlteracaoEstadoReservaCUDCache != null) {
                historicoAlteracaoEstadoReservaCUDRepository.save(historicoAlteracaoEstadoReservaCUDCache);
                redisHistoricoAlteracaoEstadoReservaCUDCache.removeCache(historicoAlteracaoEstadoReservaCUDCache.getReserva().getCodigoVoo());
            } else {
                historicoAlteracaoEstadoReservaCUDRepository.deleteByReservaCodigoReserva(reservaCUD.getCodigoReserva());
            }
        }
    }
}

package br.com.reserva.reserva.utils.conta_cud;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.models.conta_cud.HistoricoAlteracaoEstadoReservaCUD;

@Service
public class RedisHistoricoAlteracaoEstadoReservaCUDCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String HISTORICO_ALTERACAO_ESTADO_RESERVA_CACHE_KEY = "historicoalteracaoestadoreserva_cud_backup_";

    private static final long DEFAULT_CACHE_TTL = 5;

    public void saveCache(HistoricoAlteracaoEstadoReservaCUD historicoAlteracaoEstadoReserva) {
        redisTemplate.opsForValue().set(HISTORICO_ALTERACAO_ESTADO_RESERVA_CACHE_KEY + historicoAlteracaoEstadoReserva.getReserva().getCodigoReserva(), historicoAlteracaoEstadoReserva, DEFAULT_CACHE_TTL, TimeUnit.MINUTES);
    }

    public HistoricoAlteracaoEstadoReservaCUD getCache(String codigoReserva) {
        Object historicoAlteracaoEstadoReserva = redisTemplate.opsForValue().get(HISTORICO_ALTERACAO_ESTADO_RESERVA_CACHE_KEY + codigoReserva);
        if (historicoAlteracaoEstadoReserva instanceof HistoricoAlteracaoEstadoReservaCUD) {
            return (HistoricoAlteracaoEstadoReservaCUD) historicoAlteracaoEstadoReserva;
        }
        return null;
    }

    public void removeCache(String codigoReserva) {
        redisTemplate.delete(HISTORICO_ALTERACAO_ESTADO_RESERVA_CACHE_KEY + codigoReserva);
    }
}

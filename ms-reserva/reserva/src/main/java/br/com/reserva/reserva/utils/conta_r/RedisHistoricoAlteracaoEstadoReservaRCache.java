package br.com.reserva.reserva.utils.conta_r;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.models.conta_r.HistoricoAlteracaoEstadoReservaR;

@Service
public class RedisHistoricoAlteracaoEstadoReservaRCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String HISTORICO_ALTERACAO_ESTADO_RESERVA_CACHE_KEY = "historicoalteracaoestadoreserva_r_backup_";

    private static final long DEFAULT_CACHE_TTL = 5;

    public void saveCache(HistoricoAlteracaoEstadoReservaR historicoAlteracaoEstadoReserva) {
        redisTemplate.opsForValue().set(HISTORICO_ALTERACAO_ESTADO_RESERVA_CACHE_KEY + historicoAlteracaoEstadoReserva.getIdHistoricoAlteracaoEstadoReserva(), historicoAlteracaoEstadoReserva, DEFAULT_CACHE_TTL, TimeUnit.MINUTES);
    }

    public HistoricoAlteracaoEstadoReservaR getCache(Long idHistoricoAlteracaoEstadoReserva) {
        Object historicoAlteracaoEstadoReserva = redisTemplate.opsForValue().get(HISTORICO_ALTERACAO_ESTADO_RESERVA_CACHE_KEY + idHistoricoAlteracaoEstadoReserva);
        if (historicoAlteracaoEstadoReserva instanceof HistoricoAlteracaoEstadoReservaR) {
            return (HistoricoAlteracaoEstadoReservaR) historicoAlteracaoEstadoReserva;
        }
        return null;
    }

    public void removeCache(Long idHistoricoAlteracaoEstadoReserva) {
        redisTemplate.delete(HISTORICO_ALTERACAO_ESTADO_RESERVA_CACHE_KEY + idHistoricoAlteracaoEstadoReserva);
    }
}

package br.com.reserva.reserva.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.models.HistoricoAlteracaoEstadoReserva;

@Service
public class RedisHistoricoAlteracaoEstadoReservaCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String HISTORICO_ALTERACAO_ESTADO_RESERVA_CACHE_KEY = "historicoalteracaoestadoreserva_backup_";

    private static final long DEFAULT_CACHE_TTL = 5;

    public void saveCache(HistoricoAlteracaoEstadoReserva historicoAlteracaoEstadoReserva) {
        redisTemplate.opsForValue().set(HISTORICO_ALTERACAO_ESTADO_RESERVA_CACHE_KEY + historicoAlteracaoEstadoReserva.getIdHistoricoAlteracaoEstadoReserva(), historicoAlteracaoEstadoReserva, DEFAULT_CACHE_TTL, TimeUnit.MINUTES);
    }

    public HistoricoAlteracaoEstadoReserva getCache(Long idHistoricoAlteracaoEstadoReserva) {
        Object historicoAlteracaoEstadoReserva = redisTemplate.opsForValue().get(HISTORICO_ALTERACAO_ESTADO_RESERVA_CACHE_KEY + idHistoricoAlteracaoEstadoReserva);
        if (historicoAlteracaoEstadoReserva instanceof HistoricoAlteracaoEstadoReserva) {
            return (HistoricoAlteracaoEstadoReserva) historicoAlteracaoEstadoReserva;
        }
        return null;
    }

    public void removeCache(Long idHistoricoAlteracaoEstadoReserva) {
        redisTemplate.delete(HISTORICO_ALTERACAO_ESTADO_RESERVA_CACHE_KEY + idHistoricoAlteracaoEstadoReserva);
    }
}

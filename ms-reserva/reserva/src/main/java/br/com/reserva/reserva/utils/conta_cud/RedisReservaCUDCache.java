package br.com.reserva.reserva.utils.conta_cud;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.models.conta_cud.ReservaCUD;

@Service
public class RedisReservaCUDCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String RESERVA_CACHE_KEY = "reserva_cud_backup_";

    private static final long DEFAULT_CACHE_TTL = 5;

    public void saveCache(ReservaCUD reserva) {
        redisTemplate.opsForValue().set(RESERVA_CACHE_KEY + reserva.getCodigoReserva(), reserva, DEFAULT_CACHE_TTL, TimeUnit.MINUTES);
    }

    public void saveListCache(List<ReservaCUD> listaReserva, String codigoVoo) {
        String key = RESERVA_CACHE_KEY + codigoVoo;
        redisTemplate.delete(key);
        for (ReservaCUD reserva : listaReserva) {
            redisTemplate.opsForList().rightPush(key, reserva);
        }
        redisTemplate.expire(key, DEFAULT_CACHE_TTL, TimeUnit.MINUTES);
    }

    public ReservaCUD getCache(String codigoReserva) {
        Object reserva = redisTemplate.opsForValue().get(RESERVA_CACHE_KEY + codigoReserva);
        if (reserva instanceof ReservaCUD) {
            return (ReservaCUD) reserva;
        }
        return null;
    }

    public List<ReservaCUD> getListCache(String codigoVoo) {
        String key = RESERVA_CACHE_KEY + codigoVoo;
        List<Object> rawList = redisTemplate.opsForList().range(key, 0, -1);
        if (rawList != null && !rawList.isEmpty() && rawList.get(0) instanceof ReservaCUD) {
            return (List<ReservaCUD>) (List<?>) rawList;
        }
        return null;
    }

    public void removeCache(String codigoReserva) {
        redisTemplate.delete(RESERVA_CACHE_KEY + codigoReserva);
    }

    public void removeListCache(String codigoVoo) {
        String key = RESERVA_CACHE_KEY + codigoVoo;
        redisTemplate.delete(key);
    }
}

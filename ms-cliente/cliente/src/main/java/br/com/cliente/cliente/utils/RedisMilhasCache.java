package br.com.cliente.cliente.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import br.com.cliente.cliente.models.Milhas;

@Service
public class RedisMilhasCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String MILHAS_CACHE_KEY = "milhas_backup_";

    private static final long DEFAULT_CACHE_TTL = 5;

    public void saveCache(Milhas milhas) {
        redisTemplate.opsForValue().set(MILHAS_CACHE_KEY + milhas.getCodigoReserva(), milhas, DEFAULT_CACHE_TTL, TimeUnit.MINUTES);
    }

    public Milhas getCache(String codigoReserva) {
        Object milhas = redisTemplate.opsForValue().get(MILHAS_CACHE_KEY + codigoReserva);
        if (milhas instanceof Milhas) {
            return (Milhas) milhas;
        }
        return null;
    }

    public void removeCache(String codigoReserva) {
        redisTemplate.delete(MILHAS_CACHE_KEY + codigoReserva);
    }
}

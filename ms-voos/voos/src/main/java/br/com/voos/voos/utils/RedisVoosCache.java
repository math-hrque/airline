package br.com.voos.voos.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import br.com.voos.voos.models.Voo;

@Service
public class RedisVoosCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String VOO_CACHE_KEY = "voo_backup_";

    private static final long DEFAULT_CACHE_TTL = 5;

    public void saveCache(Voo voo) {
        redisTemplate.opsForValue().set(VOO_CACHE_KEY + voo.getCodigoVoo(), voo, DEFAULT_CACHE_TTL, TimeUnit.MINUTES);
    }

    public Voo getCache(String codigoVoo) {
        Object voo = redisTemplate.opsForValue().get(VOO_CACHE_KEY + codigoVoo);
        if (voo instanceof Voo) {
            return (Voo) voo;
        }
        return null;
    }

    public void removeCache(String codigoVoo) {
        redisTemplate.delete(VOO_CACHE_KEY + codigoVoo);
    }
}

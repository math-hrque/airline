package br.com.auth.auth.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import br.com.auth.auth.models.Usuario;

@Service
public class RedisUsuarioCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String USUARIO_CACHE_KEY = "usuario_backup_";

    private static final long DEFAULT_CACHE_TTL = 5;

    public void saveCache(Usuario usuario) {
        redisTemplate.opsForValue().set(USUARIO_CACHE_KEY + usuario.getId(), usuario, DEFAULT_CACHE_TTL, TimeUnit.MINUTES);
    }

    public Usuario getCache(String id) {
        Object usuario = redisTemplate.opsForValue().get(USUARIO_CACHE_KEY + id);
        if (usuario instanceof Usuario) {
            return (Usuario) usuario;
        }
        return null;
    }

    public void removeCache(String id) {
        redisTemplate.delete(USUARIO_CACHE_KEY + id);
    }
}

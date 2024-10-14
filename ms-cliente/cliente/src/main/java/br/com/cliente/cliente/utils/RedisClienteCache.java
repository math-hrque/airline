package br.com.cliente.cliente.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import br.com.cliente.cliente.models.Cliente;

@Service
public class RedisClienteCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CLIENTE_CACHE_KEY = "cliente_backup_";

    private static final long DEFAULT_CACHE_TTL = 5;

    public void saveCache(Cliente cliente) {
        redisTemplate.opsForValue().set(CLIENTE_CACHE_KEY + cliente.getIdCliente(), cliente, DEFAULT_CACHE_TTL, TimeUnit.MINUTES);
    }

    public Cliente getCache(Long idCliente) {
        Object cliente = redisTemplate.opsForValue().get(CLIENTE_CACHE_KEY + idCliente);
        if (cliente instanceof Cliente) {
            return (Cliente) cliente;
        }
        return null;
    }

    public void removeCache(Long idCliente) {
        redisTemplate.delete(CLIENTE_CACHE_KEY + idCliente);
    }
}

package br.com.funcionario.funcionario.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import br.com.funcionario.funcionario.models.Funcionario;

@Service
public class RedisFuncionarioCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String FUNCIONARIO_CACHE_KEY = "funcionario_backup_";

    private static final long DEFAULT_CACHE_TTL = 5;

    public void saveCache(Funcionario funcionario) {
        redisTemplate.opsForValue().set(FUNCIONARIO_CACHE_KEY + funcionario.getIdFuncionario(), funcionario, DEFAULT_CACHE_TTL, TimeUnit.MINUTES);
    }

    public Funcionario getCache(Long idFuncionario) {
        Object funcionario = redisTemplate.opsForValue().get(FUNCIONARIO_CACHE_KEY + idFuncionario);
        if (funcionario instanceof Funcionario) {
            return (Funcionario) funcionario;
        }
        return null;
    }

    public void removeCache(Long idFuncionario) {
        redisTemplate.delete(FUNCIONARIO_CACHE_KEY + idFuncionario);
    }
}

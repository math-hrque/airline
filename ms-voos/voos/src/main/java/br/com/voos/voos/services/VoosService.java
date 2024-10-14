package br.com.voos.voos.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.voos.voos.repositories.AeroportoRepository;
import br.com.voos.voos.repositories.EstadoVooRepository;
import br.com.voos.voos.repositories.VoosRepository;
import br.com.voos.voos.utils.RedisVoosCache;

@Service
public class VoosService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RedisVoosCache redisVoosCache;

    @Autowired
    private VoosRepository vooRepository;

    @Autowired
    private AeroportoRepository aeroportoRepository;

    @Autowired
    private EstadoVooRepository estadoVooRepository;

}

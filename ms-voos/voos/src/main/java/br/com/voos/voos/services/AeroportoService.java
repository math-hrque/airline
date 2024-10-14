package br.com.voos.voos.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.voos.voos.repositories.AeroportoRepository;

@Service
public class AeroportoService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AeroportoRepository aeroportoRepository;

}

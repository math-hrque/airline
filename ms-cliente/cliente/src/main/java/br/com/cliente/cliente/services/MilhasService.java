package br.com.cliente.cliente.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cliente.cliente.repositories.MilhasRepository;
import br.com.cliente.cliente.repositories.TransacaoRepository;


@Service
public class MilhasService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MilhasRepository milhasRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

}

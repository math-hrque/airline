package br.com.cliente.cliente.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cliente.cliente.repositories.ClienteRepository;
import br.com.cliente.cliente.repositories.MilhasRepository;

@Service
public class ClienteService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MilhasRepository milhasRepository;

}

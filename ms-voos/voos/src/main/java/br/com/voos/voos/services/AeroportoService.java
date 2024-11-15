package br.com.voos.voos.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.voos.voos.dtos.CodigoAeroportoDto;
import br.com.voos.voos.exeptions.ListaAeroportoVaziaException;
import br.com.voos.voos.models.Aeroporto;
import br.com.voos.voos.repositories.AeroportoRepository;

@Service
public class AeroportoService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AeroportoRepository aeroportoRepository;

    public List<CodigoAeroportoDto> listarAeroportos() throws ListaAeroportoVaziaException {
        List<Aeroporto> listaAeroportoBD = aeroportoRepository.findAll();
        if (listaAeroportoBD.isEmpty()) {
            throw new ListaAeroportoVaziaException("Lista de aeroportos vazia!");
        }

        List<CodigoAeroportoDto> listaAeroportoDto = listaAeroportoBD.stream().map(aeroportoBD -> mapper.map(aeroportoBD, CodigoAeroportoDto.class)).collect(Collectors.toList());
        return listaAeroportoDto;
    }
}

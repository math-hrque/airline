package br.com.voos.voos.services;

import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;
import java.time.OffsetDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.voos.voos.dtos.VooDto;
import br.com.voos.voos.dtos.VooManterDto;
import br.com.voos.voos.enums.TipoEstadoVoo;
import br.com.voos.voos.exeptions.ListaVoosVaziaException;
import br.com.voos.voos.exeptions.MudancaEstadoVooInvalidaException;
import br.com.voos.voos.exeptions.VooNaoExisteException;
import br.com.voos.voos.models.Voo;
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

    public VooManterDto realizar(String codigoVoo) throws VooNaoExisteException, MudancaEstadoVooInvalidaException {
        Optional<Voo> vooBD = vooRepository.findById(codigoVoo);
        if (!vooBD.isPresent()) {
            throw new VooNaoExisteException("Voo nao existe!");
        }
        if (vooBD.get().getEstadoVoo().getTipoEstadoVoo() != TipoEstadoVoo.CONFIRMADO) {
            throw new MudancaEstadoVooInvalidaException("Estado de Voo nao eh valido para ser realizado!");
        }

        Voo vooCache = redisVoosCache.getCache(vooBD.get().getCodigoVoo());
        if (vooCache == null) {
            redisVoosCache.saveCache(vooBD.get());
        }

        Voo voo = vooBD.get();
        voo.setEstadoVoo(estadoVooRepository.findByTipoEstadoVoo(TipoEstadoVoo.REALIZADO));
        Voo vooRealizadoBD = vooRepository.save(voo);
        VooManterDto vooManterRealizadoDto = mapper.map(vooRealizadoBD, VooManterDto.class);
        return vooManterRealizadoDto;
    }

    public VooManterDto reverterRealizado(String codigoVoo) throws VooNaoExisteException {
        Optional<Voo> vooBD = vooRepository.findById(codigoVoo);
        if (!vooBD.isPresent()) {
            throw new VooNaoExisteException("Voo nao existe!");
        }

        Voo vooCache = redisVoosCache.getCache(codigoVoo);
        if (redisVoosCache == null) {
            throw new VooNaoExisteException("Voo nao existe no cache!");
        }

        Voo voo = vooRepository.save(vooCache);
        redisVoosCache.removeCache(vooCache.getCodigoVoo());
        VooManterDto vooManterRevertidoDto = mapper.map(voo, VooManterDto.class);
        return vooManterRevertidoDto;
    }

    public List<VooDto> listarVoos48h() throws ListaVoosVaziaException {
        OffsetDateTime dataAtual = OffsetDateTime.now();
        OffsetDateTime dataLimite = dataAtual.plusHours(48);
    
        Optional<List<Voo>> listaVooBD = vooRepository.findVoosConfirmadosByProximas48Horas(dataAtual, dataLimite);
        if (!listaVooBD.isPresent() || listaVooBD.get().isEmpty()) {
            throw new ListaVoosVaziaException("Lista de voos nas próximas 48h vazia!");
        }
    
        List<VooDto> listaVooDto = listaVooBD.get().stream().map(vooBD -> mapper.map(vooBD, VooDto.class)).collect(Collectors.toList());
        return listaVooDto;
    }

    public List<VooDto> listarVoosAtuais(Optional<String> codigoAeroportoOrigem, Optional<String> codigoAeroportoDestino) throws ListaVoosVaziaException {
        OffsetDateTime dataAtual = OffsetDateTime.now();
        Optional<List<Voo>> listaVooBD = vooRepository.findVoosConfirmadosByDataVooAndAeroportos(dataAtual, codigoAeroportoOrigem.orElse(null), codigoAeroportoDestino.orElse(null));
        if (!listaVooBD.isPresent() || listaVooBD.get().isEmpty()) {
            throw new ListaVoosVaziaException("Lista de voos atuais vazia!");
        }
    
        List<VooDto> listaVooDto = listaVooBD.get().stream().map(vooBD -> mapper.map(vooBD, VooDto.class)).collect(Collectors.toList());
        return listaVooDto;
    }
}

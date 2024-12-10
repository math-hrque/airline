package br.com.voos.voos.services;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;
import java.time.OffsetDateTime;

import br.com.voos.voos.dtos.CadastrarVooDto;
import br.com.voos.voos.dtos.CodigoVooDto;
import br.com.voos.voos.exeptions.*;
import br.com.voos.voos.models.Aeroporto;
import br.com.voos.voos.repositories.AeroportoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.voos.voos.dtos.ReservaManterDto;
import br.com.voos.voos.dtos.VooDto;
import br.com.voos.voos.dtos.VooManterDto;
import br.com.voos.voos.enums.TipoEstadoVoo;
import br.com.voos.voos.models.Voo;
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
    private EstadoVooRepository estadoVooRepository;

    @Autowired
    private AeroportoRepository aeroportoRepository;

    public ReservaManterDto ocuparPoltronaVoo(ReservaManterDto reservaManterDto) throws VooNaoExisteException, LimitePoltronasOcupadasVooException {
        Optional<Voo> vooBD = vooRepository.findById(reservaManterDto.getCodigoVoo());
        if (!vooBD.isPresent()) {
            throw new VooNaoExisteException("Voo nao existe!");
        }
        if ((vooBD.get().getQuantidadePoltronasOcupadas() + reservaManterDto.getQuantidadePoltronas()) > vooBD.get().getQuantidadePoltronasTotal()) {
            throw new LimitePoltronasOcupadasVooException("Quantidade de poltronas da Reserva excede quantidade máxima de poltronas disponíveis!");
        }

        Voo vooCache = redisVoosCache.getCache(vooBD.get().getCodigoVoo());
        if (vooCache == null) {
            redisVoosCache.saveCache(vooBD.get());
        }

        Voo voo = vooBD.get();
        voo.setQuantidadePoltronasOcupadas(voo.getQuantidadePoltronasOcupadas() + reservaManterDto.getQuantidadePoltronas());
        vooRepository.atualizarPoltronasOcupadasVoo(voo);
        Optional<Voo> vooAtualizado = vooRepository.findById(voo.getCodigoVoo());
        reservaManterDto.setCodigoAeroportoOrigem(vooAtualizado.get().getAeroportoOrigem().getCodigoAeroporto());
        reservaManterDto.setCodigoAeroportoDestino(vooAtualizado.get().getAeroportoDestino().getCodigoAeroporto());
        return reservaManterDto;
    }

    public VooManterDto reverterVooPoltronaOcupada(ReservaManterDto reservaManterDto) throws VooNaoExisteException {
        Optional<Voo> vooBD = vooRepository.findById(reservaManterDto.getCodigoVoo());
        if (!vooBD.isPresent()) {
            throw new VooNaoExisteException("Voo nao existe!");
        }

        Voo vooCache = redisVoosCache.getCache(reservaManterDto.getCodigoVoo());
        if (redisVoosCache == null) {
            throw new VooNaoExisteException("Voo nao existe no cache!");
        }

        vooRepository.atualizarPoltronasOcupadasVoo(vooCache);
        redisVoosCache.removeCache(vooCache.getCodigoVoo());
        Optional<Voo> voo = vooRepository.findById(vooBD.get().getCodigoVoo());
        VooManterDto vooManterRevertidoDto = mapper.map(voo.get(), VooManterDto.class);
        return vooManterRevertidoDto;
    }

    public ReservaManterDto desocuparPoltronaVoo(ReservaManterDto reservaManterDto) throws VooNaoExisteException {
        Optional<Voo> vooBD = vooRepository.findById(reservaManterDto.getCodigoVoo());
        if (!vooBD.isPresent()) {
            throw new VooNaoExisteException("Voo nao existe!");
        }

        Voo vooCache = redisVoosCache.getCache(vooBD.get().getCodigoVoo());
        if (vooCache == null) {
            redisVoosCache.saveCache(vooBD.get());
        }

        Voo voo = vooBD.get();
        voo.setQuantidadePoltronasOcupadas(voo.getQuantidadePoltronasOcupadas() - reservaManterDto.getQuantidadePoltronas());
        vooRepository.atualizarPoltronasOcupadasVoo(voo);
        Optional<Voo> vooAtualizado = vooRepository.findById(voo.getCodigoVoo());
        reservaManterDto.setCodigoAeroportoOrigem(vooAtualizado.get().getAeroportoOrigem().getCodigoAeroporto());
        reservaManterDto.setCodigoAeroportoDestino(vooAtualizado.get().getAeroportoDestino().getCodigoAeroporto());
        return reservaManterDto;
    }

    public VooManterDto reverterVooPoltronaDesocupado(ReservaManterDto reservaManterDto) throws VooNaoExisteException {
        Optional<Voo> vooBD = vooRepository.findById(reservaManterDto.getCodigoVoo());
        if (!vooBD.isPresent()) {
            throw new VooNaoExisteException("Voo nao existe!");
        }

        Voo vooCache = redisVoosCache.getCache(reservaManterDto.getCodigoVoo());
        if (redisVoosCache == null) {
            throw new VooNaoExisteException("Voo nao existe no cache!");
        }

        vooRepository.atualizarPoltronasOcupadasVoo(vooCache);
        redisVoosCache.removeCache(vooCache.getCodigoVoo());
        Optional<Voo> voo = vooRepository.findById(vooBD.get().getCodigoVoo());
        VooManterDto vooManterRevertidoDto = mapper.map(voo.get(), VooManterDto.class);
        return vooManterRevertidoDto;
    }

    public VooManterDto cancelar(String codigoVoo) throws VooNaoExisteException, MudancaEstadoVooInvalidaException {
        Optional<Voo> vooBD = vooRepository.findById(codigoVoo);
        if (!vooBD.isPresent()) {
            throw new VooNaoExisteException("Voo nao existe!");
        }
        if (vooBD.get().getEstadoVoo().getTipoEstadoVoo() != TipoEstadoVoo.CONFIRMADO) {
            throw new MudancaEstadoVooInvalidaException("Estado de Voo nao eh valido para ser cancelado!");
        }

        Voo vooCache = redisVoosCache.getCache(vooBD.get().getCodigoVoo());
        if (vooCache == null) {
            redisVoosCache.saveCache(vooBD.get());
        }

        Voo voo = vooBD.get();
        voo.setEstadoVoo(estadoVooRepository.findByTipoEstadoVoo(TipoEstadoVoo.CANCELADO));
        Voo vooRealizadoBD = vooRepository.save(voo);
        VooManterDto vooManterRealizadoDto = mapper.map(vooRealizadoBD, VooManterDto.class);
        return vooManterRealizadoDto;
    }

    public VooManterDto reverterCancelado(String codigoVoo) throws VooNaoExisteException {
        Optional<Voo> vooBD = vooRepository.findById(codigoVoo);
        if (!vooBD.isPresent()) {
            throw new VooNaoExisteException("Voo nao existe!");
        }

        Voo vooCache = redisVoosCache.getCache(codigoVoo);
        if (redisVoosCache == null) {
            throw new VooNaoExisteException("Voo nao existe no cache!");
        }

        vooRepository.updateVoo(vooCache);
        redisVoosCache.removeCache(vooCache.getCodigoVoo());
        VooManterDto vooManterRevertidoDto = mapper.map(vooCache, VooManterDto.class);
        return vooManterRevertidoDto;
    }

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

        vooRepository.updateVoo(vooCache);
        redisVoosCache.removeCache(vooCache.getCodigoVoo());
        VooManterDto vooManterRevertidoDto = mapper.map(vooCache, VooManterDto.class);
        return vooManterRevertidoDto;
    }

    public VooDto cadastrarVoo(CadastrarVooDto cadastrarVooDto) throws VooJaExisteException, AeroportoNaoExisteException, DateTimeParseException, VooValidationException {
        validateCadastrarVooDto(cadastrarVooDto);

        Optional<Voo> vooBD = vooRepository.findVooByCodigo(cadastrarVooDto.getCodigoVoo());
        if (vooBD.isPresent()) {
            throw new VooJaExisteException("Voo já existe com o código " + cadastrarVooDto.getCodigoVoo() + "!");
        }

        Optional<Aeroporto> aeroportoOrigemBD = aeroportoRepository.findAeroportoByCodigo(cadastrarVooDto.getAeroportoOrigem());
        if (aeroportoOrigemBD.isEmpty()) {
            throw new AeroportoNaoExisteException("Aeroporto não existe com o código " + cadastrarVooDto.getAeroportoOrigem() + "!");
        }

        Optional<Aeroporto> aeroportoDestinoBD = aeroportoRepository.findAeroportoByCodigo(cadastrarVooDto.getAeroportoDestino());
        if (aeroportoDestinoBD.isEmpty()) {
            throw new AeroportoNaoExisteException("Aeroporto não existe com o código " + cadastrarVooDto.getAeroportoDestino() + "!");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        Voo voo = new Voo();
        voo.setCodigoVoo(cadastrarVooDto.getCodigoVoo());
        voo.setDataVoo(OffsetDateTime.parse(cadastrarVooDto.getDataHora(), formatter).withOffsetSameInstant(ZoneOffset.UTC));
        voo.setValorPassagem(cadastrarVooDto.getValorPassagemReais());
        voo.setQuantidadePoltronasTotal(cadastrarVooDto.getQuantidadePoltronasTotal());
        voo.setQuantidadePoltronasOcupadas(0);
        voo.setAeroportoOrigem(aeroportoOrigemBD.get());
        voo.setAeroportoDestino(aeroportoDestinoBD.get());
        voo.setEstadoVoo(estadoVooRepository.findByTipoEstadoVoo(TipoEstadoVoo.CONFIRMADO));
        vooRepository.save(voo);

        return mapper.map(voo, VooDto.class);
    }

    public List<VooDto> listarVoosConfirmados48h() throws ListaVoosVaziaException {
        OffsetDateTime dataAtual = OffsetDateTime.now();
        OffsetDateTime dataLimite = dataAtual.plusHours(48);
    
        Optional<List<Voo>> listaVooBD = vooRepository.findVoosConfirmadosByProximas48Horas(dataAtual, dataLimite);
        if (!listaVooBD.isPresent() || listaVooBD.get().isEmpty()) {
            throw new ListaVoosVaziaException("Lista de voos confirmados nas próximas 48h vazia!");
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

    private void validateCadastrarVooDto(CadastrarVooDto cadastrarVooDto) throws VooValidationException {
        if (cadastrarVooDto.getCodigoVoo() == null || cadastrarVooDto.getCodigoVoo().isEmpty()) {
            throw new VooValidationException("O código do voo é obrigatório.");
        }
        if (cadastrarVooDto.getDataHora() == null || cadastrarVooDto.getDataHora().isEmpty()) {
            throw new VooValidationException("A data e hora do voo são obrigatórias.");
        }
        if (cadastrarVooDto.getValorPassagemReais() == null || cadastrarVooDto.getValorPassagemReais() <= 0) {
            throw new VooValidationException("O valor da passagem deve ser positivo e não pode ser nulo.");
        }
        if (cadastrarVooDto.getQuantidadePoltronasTotal() == null || cadastrarVooDto.getQuantidadePoltronasTotal() <= 0) {
            throw new VooValidationException("A quantidade de poltronas deve ser maior que zero e não pode ser nula.");
        }
        if (cadastrarVooDto.getAeroportoOrigem() == null || cadastrarVooDto.getAeroportoOrigem().isEmpty()) {
            throw new VooValidationException("O código do aeroporto de origem é obrigatório.");
        }
        if (cadastrarVooDto.getAeroportoDestino() == null || cadastrarVooDto.getAeroportoDestino().isEmpty()) {
            throw new VooValidationException("O código do aeroporto de destino é obrigatório.");
        }
    }

    public VooDto visualizarVoo(String codigoVoo) throws VooNaoExisteException {
      Optional<Voo> vooOptional = vooRepository.findVooByCodigo(codigoVoo);
  
      if (!vooOptional.isPresent()) {
          throw new VooNaoExisteException("Nenhum voo encontrado para o código especificado.");
      }
      return mapper.map(vooOptional.get(), VooDto.class);
    }

    public List<VooDto> listarVoosPorCodigos(List<CodigoVooDto> listaCodigoVoo) throws ListaVoosVaziaException {
        List<String> codigosVoo = listaCodigoVoo.stream().map(CodigoVooDto::getCodigoVoo).collect(Collectors.toList());
        List<Voo> listaVoos = vooRepository.findByCodigoVooIn(codigosVoo);
    
        if (listaVoos == null || listaVoos.isEmpty()) {
            throw new ListaVoosVaziaException("Nenhum voo encontrado para os códigos especificados.");
        }
    
        return listaVoos.stream().map(voo -> mapper.map(voo, VooDto.class)).collect(Collectors.toList());
    }

    public List<VooDto> listarVoosRealizadosCancelados() throws ListaVoosVaziaException {
        List<Voo> listaVoos = vooRepository.findByEstadoVooTipoEstadoVooIn(List.of(TipoEstadoVoo.REALIZADO, TipoEstadoVoo.CANCELADO));
    
        if (listaVoos == null || listaVoos.isEmpty()) {
            throw new ListaVoosVaziaException("Nenhum voo realizado ou confirmado encontrado.");
        }
    
        return listaVoos.stream().map(voo -> mapper.map(voo, VooDto.class)).collect(Collectors.toList());
    }
}

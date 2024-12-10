package br.com.reserva.reserva.services.conta_r;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.dtos.CodigoVooDto;
import br.com.reserva.reserva.dtos.ReservaDto;
import br.com.reserva.reserva.dtos.ReservaManterDto;
import br.com.reserva.reserva.dtos.VooManterDto;
import br.com.reserva.reserva.enums.TipoEstadoReserva;
import br.com.reserva.reserva.exeptions.ListaReservaVaziaException;
import br.com.reserva.reserva.exeptions.ReservaNaoExisteException;
import br.com.reserva.reserva.models.conta_cud.ReservaCUD;
import br.com.reserva.reserva.models.conta_r.ReservaR;
import br.com.reserva.reserva.repositories.conta_r.ReservaRRepository;
import br.com.reserva.reserva.utils.conta_r.RedisReservaRCache;

@Service
public class ReservaRService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RedisReservaRCache redisReservaRCache;

    @Autowired
    private ReservaRRepository reservaRRepository;

    public ReservaManterDto reservaRCadastrar(ReservaCUD reservaCUD) {
        ReservaR reservaR = mapper.map(reservaCUD, ReservaR.class);
        reservaR.setSiglaEstadoReserva(reservaCUD.getEstadoReserva().getSiglaEstadoReserva());
        reservaR.setTipoEstadoReserva(reservaCUD.getEstadoReserva().getTipoEstadoReserva());
        ReservaR reservaEmbarcadaR = reservaRRepository.save(reservaR);
        ReservaManterDto reservaManterEmbarcadaR = mapper.map(reservaEmbarcadaR, ReservaManterDto.class);
        return reservaManterEmbarcadaR;
    }

    public ReservaManterDto reservaRDeletar(String codigoReserva) throws ReservaNaoExisteException {
        Optional<ReservaR> reservaRBD = reservaRRepository.findById(codigoReserva);
        if (!reservaRBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        reservaRRepository.delete(reservaRBD.get());
        ReservaManterDto reservaManterDeletadaR = mapper.map(reservaRBD.get(), ReservaManterDto.class);
        return reservaManterDeletadaR;
    }

    public ReservaManterDto reservaRCancelar(ReservaCUD reservaCUD) throws ReservaNaoExisteException {
        Optional<ReservaR> reservaRBD = reservaRRepository.findById(reservaCUD.getCodigoReserva());
        if (!reservaRBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        ReservaR reservaRCache = redisReservaRCache.getCache(reservaRBD.get().getCodigoReserva());
        if (reservaRCache == null) {
            redisReservaRCache.saveCache(reservaRBD.get());
        }

        reservaRBD.get().setSiglaEstadoReserva(reservaCUD.getEstadoReserva().getSiglaEstadoReserva());
        reservaRBD.get().setTipoEstadoReserva(reservaCUD.getEstadoReserva().getTipoEstadoReserva());
        ReservaR reservaCanceladaR = reservaRRepository.save(reservaRBD.get());
        ReservaManterDto reservaManterCanceladaR = mapper.map(reservaCanceladaR, ReservaManterDto.class);
        return reservaManterCanceladaR;
    }

    public ReservaManterDto reservaRFazerCheckin(ReservaCUD reservaCUD) throws ReservaNaoExisteException {
        Optional<ReservaR> reservaRBD = reservaRRepository.findById(reservaCUD.getCodigoReserva());
        if (!reservaRBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        ReservaR reservaRCache = redisReservaRCache.getCache(reservaRBD.get().getCodigoReserva());
        if (reservaRCache == null) {
            redisReservaRCache.saveCache(reservaRBD.get());
        }

        reservaRBD.get().setSiglaEstadoReserva(reservaCUD.getEstadoReserva().getSiglaEstadoReserva());
        reservaRBD.get().setTipoEstadoReserva(reservaCUD.getEstadoReserva().getTipoEstadoReserva());
        ReservaR reservaCheckinR = reservaRRepository.save(reservaRBD.get());
        ReservaManterDto reservaManterCheckinR = mapper.map(reservaCheckinR, ReservaManterDto.class);
        return reservaManterCheckinR;
    }

    public ReservaManterDto reservaRConfirmarEmbarque(ReservaCUD reservaCUD) throws ReservaNaoExisteException {
        Optional<ReservaR> reservaRBD = reservaRRepository.findById(reservaCUD.getCodigoReserva());
        if (!reservaRBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        ReservaR reservaRCache = redisReservaRCache.getCache(reservaRBD.get().getCodigoReserva());
        if (reservaRCache == null) {
            redisReservaRCache.saveCache(reservaRBD.get());
        }

        reservaRBD.get().setSiglaEstadoReserva(reservaCUD.getEstadoReserva().getSiglaEstadoReserva());
        reservaRBD.get().setTipoEstadoReserva(reservaCUD.getEstadoReserva().getTipoEstadoReserva());
        ReservaR reservaEmbarcadaR = reservaRRepository.save(reservaRBD.get());
        ReservaManterDto reservaManterEmbarcadaR = mapper.map(reservaEmbarcadaR, ReservaManterDto.class);
        return reservaManterEmbarcadaR;
    }

    public ReservaManterDto reservaRCompensar(String codigoReserva) throws ReservaNaoExisteException {
        Optional<ReservaR> reservaRBD = reservaRRepository.findById(codigoReserva);
        if (!reservaRBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        ReservaR reservaRCache = redisReservaRCache.getCache(reservaRBD.get().getCodigoReserva());
        if (reservaRCache == null) {
            throw new ReservaNaoExisteException("Reserva nao existe no cache!");
        }

        reservaRRepository.save(reservaRCache);
        redisReservaRCache.removeCache(reservaRCache.getCodigoReserva());
        Optional<ReservaR> ReservarRRevertida = reservaRRepository.findById(reservaRBD.get().getCodigoReserva());
        ReservaManterDto reservaManterRevertidaDto = mapper.map(ReservarRRevertida.get(), ReservaManterDto.class);
        return reservaManterRevertidaDto;
    }

    public List<ReservaManterDto> reservasRVooCancelar(VooManterDto vooManterDto) {
        List<ReservaR> listaReservaR = new ArrayList<>();
        List<ReservaManterDto> listaReservaManterDto = new ArrayList<>();
        List<ReservaR> listaReservaRCache = redisReservaRCache.getListCache(vooManterDto.getCodigoVoo());
        if (listaReservaRCache != null) {
            Optional<List<ReservaR>> listaReservaRBD = reservaRRepository.findByCodigoVoo(vooManterDto.getCodigoVoo());
            if (listaReservaRBD.isPresent()) {
                redisReservaRCache.saveListCache(listaReservaRBD.get(), vooManterDto.getCodigoVoo());
                for (ReservaR reservaR : listaReservaRCache) {
                    Optional<ReservaR> reservaRBD = reservaRRepository.findById(reservaR.getCodigoReserva());
                    if (reservaRBD.isPresent()) {
                        reservaRBD.get().setSiglaEstadoReserva(reservaR.getSiglaEstadoReserva());
                        reservaRBD.get().setTipoEstadoReserva(reservaR.getTipoEstadoReserva());
                        listaReservaR.add(reservaR);
                        ReservaManterDto reservaManterCanceladaR = mapper.map(reservaR, ReservaManterDto.class);
                        listaReservaManterDto.add(reservaManterCanceladaR);
                    }
                }
                reservaRRepository.saveAll(listaReservaR);
            }
        }
        return listaReservaManterDto;
    }

    public List<ReservaManterDto> reservasRVooRealizar(VooManterDto vooManterDto) {
        List<ReservaR> listaReservaR = new ArrayList<>();
        List<ReservaManterDto> listaReservaManterDto = new ArrayList<>();
        List<ReservaR> listaReservaRCache = redisReservaRCache.getListCache(vooManterDto.getCodigoVoo());
        if (listaReservaRCache != null) {
            Optional<List<ReservaR>> listaReservaRBD = reservaRRepository.findByCodigoVoo(vooManterDto.getCodigoVoo());
            if (listaReservaRBD.isPresent()) {
                redisReservaRCache.saveListCache(listaReservaRBD.get(), vooManterDto.getCodigoVoo());
                for (ReservaR reservaR : listaReservaRCache) {
                    Optional<ReservaR> reservaRBD = reservaRRepository.findById(reservaR.getCodigoReserva());
                    if (reservaRBD.isPresent()) {
                        reservaRBD.get().setSiglaEstadoReserva(reservaR.getSiglaEstadoReserva());
                        reservaRBD.get().setTipoEstadoReserva(reservaR.getTipoEstadoReserva());
                        listaReservaR.add(reservaR);
                        ReservaManterDto reservaManterRealizadaR = mapper.map(reservaR, ReservaManterDto.class);
                        listaReservaManterDto.add(reservaManterRealizadaR);
                    }
                }
                reservaRRepository.saveAll(listaReservaR);
            }
        }
        return listaReservaManterDto;
    }

    public List<ReservaManterDto> reservasRVooCompensar(VooManterDto vooManterDto) {
        List<ReservaManterDto> listaReservaManterDto = new ArrayList<>();
        List<ReservaR> listaReservaRCache = redisReservaRCache.getListCache(vooManterDto.getCodigoVoo());
        if (listaReservaRCache != null) {
            reservaRRepository.saveAll(listaReservaRCache);
            redisReservaRCache.removeListCache(vooManterDto.getCodigoVoo());
            listaReservaManterDto = listaReservaRCache.stream()
                    .map(reservaR -> mapper.map(reservaR, ReservaManterDto.class)).collect(Collectors.toList());
        }
        return listaReservaManterDto;
    }

    public List<ReservaDto> listarReservasVoos48h(Long idCliente, List<CodigoVooDto> listaCodigoVoo) throws ListaReservaVaziaException {
        List<String> codigosVoo = listaCodigoVoo.stream().map(CodigoVooDto::getCodigoVoo).collect(Collectors.toList());
        Optional<List<ReservaR>> listaReservaRBD = reservaRRepository.findByCodigoVooInAndIdClienteAndTipoEstadoReserva(codigosVoo, idCliente, TipoEstadoReserva.CONFIRMADO);

        if (!listaReservaRBD.isPresent() || listaReservaRBD.get().isEmpty()) {
            throw new ListaReservaVaziaException("Lista de reservas vazia!");
        }

        return listaReservaRBD.get().stream().map(reservaR -> {
            ReservaDto reservaDto = mapper.map(reservaR, ReservaDto.class);
            return reservaDto;
        }).collect(Collectors.toList());
    }

    public List<ReservaDto> listarReservasVoosRealizadosCancelados(Long idCliente, List<CodigoVooDto> listaCodigoVoo) throws ListaReservaVaziaException {
        List<String> codigosVoo = listaCodigoVoo.stream().map(CodigoVooDto::getCodigoVoo).collect(Collectors.toList());
        Optional<List<ReservaR>> listaReservaRBD = reservaRRepository.findByCodigoVooInAndIdCliente(codigosVoo, idCliente);

        if (!listaReservaRBD.isPresent() || listaReservaRBD.get().isEmpty()) {
            throw new ListaReservaVaziaException("Lista de reservas vazia!");
        }

        return listaReservaRBD.get().stream().map(reservaR -> {
            ReservaDto reservaDto = mapper.map(reservaR, ReservaDto.class);
            return reservaDto;
        }).collect(Collectors.toList());
    }

    public ReservaDto visualizarReservaCliente(String codigoReserva) throws ReservaNaoExisteException {
        Optional<ReservaR> reservaOptional = reservaRRepository.findByCodigoReserva(codigoReserva);

        if (!reservaOptional.isPresent()) {
            throw new ReservaNaoExisteException("Reserva não encontrada!");
        }

        return mapper.map(reservaOptional.get(), ReservaDto.class);
    }

    public List<ReservaDto> listarTodasReservasPorCliente(Long idCliente) throws ListaReservaVaziaException {
        Optional<List<ReservaR>> listaReserva = reservaRRepository.findByIdCliente(idCliente);

        if (!listaReserva.isPresent() || listaReserva.get().isEmpty()) {
            throw new ListaReservaVaziaException("Lista de Reservas vazias para idCliente: " + idCliente);
        }

        return listaReserva.get().stream().map(reserva -> mapper.map(reserva, ReservaDto.class)).collect(Collectors.toList());
    }

    public List<ReservaDto> listarTodasReservasConfirmadasPorCliente(Long idCliente) throws ListaReservaVaziaException {
        Optional<List<ReservaR>> listaReserva = reservaRRepository.findByIdClienteAndTipoEstadoReserva(idCliente, TipoEstadoReserva.CONFIRMADO);

        if (!listaReserva.isPresent() || listaReserva.get().isEmpty()) {
            throw new ListaReservaVaziaException("Lista de Reservas confirmadas vazias para idCliente: " + idCliente);
        }

        return listaReserva.get().stream().map(reserva -> mapper.map(reserva, ReservaDto.class)).collect(Collectors.toList());
    }
}

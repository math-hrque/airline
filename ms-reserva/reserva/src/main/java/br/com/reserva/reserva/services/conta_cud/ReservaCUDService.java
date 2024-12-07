package br.com.reserva.reserva.services.conta_cud;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.dtos.ReservaManterDto;
import br.com.reserva.reserva.dtos.VooManterDto;
import br.com.reserva.reserva.enums.TipoEstadoReserva;
import br.com.reserva.reserva.exeptions.MudancaEstadoReservaInvalidaException;
import br.com.reserva.reserva.exeptions.ReservaNaoExisteException;
import br.com.reserva.reserva.models.conta_cud.ReservaCUD;
import br.com.reserva.reserva.models.conta_r.ReservaR;
import br.com.reserva.reserva.repositories.conta_cud.EstadoReservaCUDRepository;
import br.com.reserva.reserva.repositories.conta_cud.ReservaCUDRepository;
import br.com.reserva.reserva.utils.conta_cud.CodigoReservaGenerator;
import br.com.reserva.reserva.utils.conta_cud.RedisReservaCUDCache;
import br.com.reserva.reserva.utils.conta_r.RedisReservaRCache;

@Service
public class ReservaCUDService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "saga-exchange";

    @Autowired
    private RedisReservaCUDCache redisReservaCUDCache;

    @Autowired
    private RedisReservaRCache redisReservaRCache;

    @Autowired
    private ReservaCUDRepository reservaCUDRepository;

    @Autowired
    private EstadoReservaCUDRepository estadoReservaCUDRepository;

    @Autowired
    private HistoricoAlteracaoEstadoReservaCUDService historicoAlteracaoEstadoReservaCUDService;

    @Autowired
    private CodigoReservaGenerator codigoReservaGenerator;

    public ReservaManterDto reservaCUDCadastrar(ReservaManterDto reservaManterDto) throws ReservaNaoExisteException {
        ReservaCUD reservaCUD = mapper.map(reservaManterDto, ReservaCUD.class);
        reservaCUD.setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.CONFIRMADO));
        reservaCUD.setCodigoReserva(codigoReservaGenerator.gerarCodigoReservaUnico());
        reservaCUDRepository.saveReserva(reservaCUD);

        Optional<ReservaCUD> reservaCUDCriada = reservaCUDRepository.findById(reservaCUD.getCodigoReserva());
        if (!reservaCUDCriada.isPresent()) {
            throw new ReservaNaoExisteException("Reserva " + reservaCUD.getCodigoReserva() + " nao foi criada");
        }
        
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cadastrada-contaR", reservaCUDCriada.get());
        ReservaManterDto reservaManterCriadaDto = mapper.map(reservaCUDCriada.get(), ReservaManterDto.class);
        return reservaManterCriadaDto;
    }

    public ReservaManterDto reverterReservaCUDcadastrada(ReservaManterDto reservaManterDto) throws ReservaNaoExisteException {
        if (reservaManterDto.getCodigoReserva() != null) {
            Optional<ReservaCUD> reservaCUDBD = reservaCUDRepository.findById(reservaManterDto.getCodigoReserva());
            if (!reservaCUDBD.isPresent()) {
                throw new ReservaNaoExisteException("Reserva nao existe");
            }
            reservaCUDRepository.deleteById(reservaManterDto.getCodigoReserva());
        }
        return reservaManterDto;
    }

    public ReservaManterDto reservaCUDCancelar(String codigoReserva) throws ReservaNaoExisteException, MudancaEstadoReservaInvalidaException {
        Optional<ReservaCUD> reservaCUDBD = reservaCUDRepository.findById(codigoReserva);
        if (!reservaCUDBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        if (reservaCUDBD.get().getEstadoReserva().getTipoEstadoReserva() != TipoEstadoReserva.CONFIRMADO) {
            throw new MudancaEstadoReservaInvalidaException("Estado de Reserva nao eh valido para ser Cancelada!");
        }

        ReservaCUD reservaCUDCache = redisReservaCUDCache.getCache(reservaCUDBD.get().getCodigoReserva());
        if (reservaCUDCache == null) {
            redisReservaCUDCache.saveCache(reservaCUDBD.get());
        }

        historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUDBD.get(), TipoEstadoReserva.CANCELADO);
        reservaCUDBD.get().setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.CANCELADO));
        ReservaCUD reservaCanceladaCUD = reservaCUDRepository.save(reservaCUDBD.get());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cancelada-contaR", reservaCanceladaCUD);
        ReservaManterDto reservaManterCanceladaCUD = mapper.map(reservaCanceladaCUD, ReservaManterDto.class);
        return reservaManterCanceladaCUD;
    }

    public ReservaManterDto reverterReservaCUDCancelada(String codigoReserva) throws ReservaNaoExisteException {
        Optional<ReservaCUD> reservaCUDBD = reservaCUDRepository.findById(codigoReserva);
        if (!reservaCUDBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        ReservaCUD reservaCUDCache = redisReservaCUDCache.getCache(reservaCUDBD.get().getCodigoReserva());
        if (reservaCUDCache == null) {
            throw new ReservaNaoExisteException("Reserva nao existe no cache!");
        }

        reservaCUDRepository.updateReserva(reservaCUDCache);
        redisReservaCUDCache.removeCache(reservaCUDCache.getCodigoReserva());
        historicoAlteracaoEstadoReservaCUDService.reverterHistoricoEstadoReserva(Collections.singletonList(reservaCUDCache));
        Optional<ReservaCUD> ReservaCUDRevertida = reservaCUDRepository.findById(reservaCUDBD.get().getCodigoReserva());
        ReservaManterDto reservaManterRevertidaDto = mapper.map(ReservaCUDRevertida.get(), ReservaManterDto.class);
        return reservaManterRevertidaDto;
    }

    public ReservaManterDto fazerCheckinReservaCUD(String codigoReserva) throws ReservaNaoExisteException, MudancaEstadoReservaInvalidaException {
        Optional<ReservaCUD> reservaCUDBD = reservaCUDRepository.findById(codigoReserva);
        if (!reservaCUDBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        if (reservaCUDBD.get().getEstadoReserva().getTipoEstadoReserva() != TipoEstadoReserva.CONFIRMADO) {
            throw new MudancaEstadoReservaInvalidaException("Estado de Reserva nao eh valido para ser Check-in!");
        }

        ReservaCUD reservaCUDCache = redisReservaCUDCache.getCache(reservaCUDBD.get().getCodigoReserva());
        if (reservaCUDCache == null) {
            redisReservaCUDCache.saveCache(reservaCUDBD.get());
        }

        historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUDBD.get(), TipoEstadoReserva.CHECK_IN);
        reservaCUDBD.get().setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.CHECK_IN));
        ReservaCUD reservaCheckinCUD = reservaCUDRepository.save(reservaCUDBD.get());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-fazer-checkin-contaR", reservaCheckinCUD);
        ReservaManterDto reservaManterCheckinCUD = mapper.map(reservaCheckinCUD, ReservaManterDto.class);
        return reservaManterCheckinCUD;
    }

    public ReservaManterDto confirmarEmbarqueReservaCUD(String codigoVoo, String codigoReserva) throws ReservaNaoExisteException, MudancaEstadoReservaInvalidaException {
        Optional<ReservaCUD> reservaCUDBD = reservaCUDRepository.findByCodigoReservaAndCodigoVoo(codigoReserva, codigoVoo);
        if (!reservaCUDBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        if (reservaCUDBD.get().getEstadoReserva().getTipoEstadoReserva() != TipoEstadoReserva.CHECK_IN) {
            throw new MudancaEstadoReservaInvalidaException("Estado de Reserva nao eh valido para ser Embarcado!");
        }

        ReservaCUD reservaCUDCache = redisReservaCUDCache.getCache(reservaCUDBD.get().getCodigoReserva());
        if (reservaCUDCache == null) {
            redisReservaCUDCache.saveCache(reservaCUDBD.get());
        }

        historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUDBD.get(), TipoEstadoReserva.EMBARCADO);
        reservaCUDBD.get().setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.EMBARCADO));
        ReservaCUD reservaEmbarcadaCUD = reservaCUDRepository.save(reservaCUDBD.get());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-confirmar-embarque-contaR", reservaEmbarcadaCUD);
        ReservaManterDto reservaManterEmbarcadaCUD = mapper.map(reservaEmbarcadaCUD, ReservaManterDto.class);
        return reservaManterEmbarcadaCUD;
    }

    public ReservaManterDto reservaCUDCompensar(ReservaCUD reservaCUD) throws ReservaNaoExisteException {
        Optional<ReservaCUD> reservaCUDBD = reservaCUDRepository.findById(reservaCUD.getCodigoReserva());
        if (!reservaCUDBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        ReservaCUD reservaCUDCache = redisReservaCUDCache.getCache(reservaCUDBD.get().getCodigoReserva());
        if (reservaCUDCache == null) {
            throw new ReservaNaoExisteException("Reserva nao existe no cache!");
        }

        reservaCUDRepository.updateReserva(reservaCUDCache);
        redisReservaCUDCache.removeCache(reservaCUDCache.getCodigoReserva());
        historicoAlteracaoEstadoReservaCUDService.reverterHistoricoEstadoReserva(Collections.singletonList(reservaCUDCache));
        Optional<ReservaCUD> ReservarCUDRevertida = reservaCUDRepository.findById(reservaCUDBD.get().getCodigoReserva());
        ReservaManterDto reservaManterRevertidaDto = mapper.map(ReservarCUDRevertida.get(), ReservaManterDto.class);
        return reservaManterRevertidaDto;
    }

    public List<ReservaManterDto> cancelarReservasCUDVoo(VooManterDto vooManterDto) {
        Optional<List<ReservaCUD>> listaReservaCUDBD = reservaCUDRepository.findByCodigoVoo(vooManterDto.getCodigoVoo());
        List<ReservaManterDto> listaReservaManterDto = new ArrayList<>();

        if (listaReservaCUDBD.isPresent()) {
            List<ReservaCUD> listaReservaCUDCache = redisReservaCUDCache.getListCache(listaReservaCUDBD.get().get(0).getCodigoVoo());
            if (listaReservaCUDCache == null) {
                redisReservaCUDCache.saveListCache(listaReservaCUDBD.get(), listaReservaCUDBD.get().get(0).getCodigoVoo());
            }
            for (ReservaCUD reservaCUD : listaReservaCUDBD.get()) {
                switch (reservaCUD.getEstadoReserva().getTipoEstadoReserva()) {
                    case CONFIRMADO:
                    case CHECK_IN:
                    case EMBARCADO:
                        historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUD, TipoEstadoReserva.CANCELADO_VOO);
                        reservaCUD.setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.CANCELADO_VOO));
                        break;
                    default:
                        break;
                }
                ReservaManterDto reservaManterDto = mapper.map(reservaCUD, ReservaManterDto.class);
                reservaManterDto.setCodigoAeroportoOrigem(vooManterDto.getCodigoAeroportoOrigem());
                reservaManterDto.setCodigoAeroportoDestino(vooManterDto.getCodigoAeroportoDestino());
                listaReservaManterDto.add(reservaManterDto);
            }
            List<ReservaR> listaReservaR = listaReservaCUDBD.get().stream().map(reservaCUDBD -> mapper.map(reservaCUDBD, ReservaR.class)).collect(Collectors.toList());
            redisReservaRCache.saveListCache(listaReservaR, vooManterDto.getCodigoVoo());
            reservaCUDRepository.saveAll(listaReservaCUDBD.get());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reservas-canceladas-voo-contaR", vooManterDto);
        }
        return listaReservaManterDto; 
    }

    public void reverterReservasCUDCanceladasVoo(String codigoVoo) {
        List<ReservaCUD> listaReservaCUDCache = redisReservaCUDCache.getListCache(codigoVoo);
        if (listaReservaCUDCache != null) {
            for (ReservaCUD reservaCUD : listaReservaCUDCache) {
                reservaCUDRepository.updateReserva(reservaCUD);
            }
            redisReservaCUDCache.removeListCache(codigoVoo);
            historicoAlteracaoEstadoReservaCUDService.reverterHistoricoEstadoReserva(listaReservaCUDCache);
        }
    }

    public VooManterDto realizarReservasCUDVoo(VooManterDto vooManterDto) {
        Optional<List<ReservaCUD>> listaReservaCUDBD = reservaCUDRepository.findByCodigoVoo(vooManterDto.getCodigoVoo());

        if (listaReservaCUDBD.isPresent()) {
            List<ReservaCUD> listaReservaCUDCache = redisReservaCUDCache.getListCache(listaReservaCUDBD.get().get(0).getCodigoVoo());
            if (listaReservaCUDCache == null) {
                redisReservaCUDCache.saveListCache(listaReservaCUDBD.get(), listaReservaCUDBD.get().get(0).getCodigoVoo());
            }
            for (ReservaCUD reservaCUD : listaReservaCUDBD.get()) {
                switch (reservaCUD.getEstadoReserva().getTipoEstadoReserva()) {
                    case EMBARCADO:
                        historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUD, TipoEstadoReserva.REALIZADO);
                        reservaCUD.setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.REALIZADO));
                        break;
                    case CONFIRMADO:
                    case CHECK_IN:
                        historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUD, TipoEstadoReserva.NAO_REALIZADO);
                        reservaCUD.setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.NAO_REALIZADO));
                        break;
                    default:
                        break;
                }
            }
            List<ReservaR> listaReservaR = listaReservaCUDBD.get().stream().map(reservaCUDBD -> mapper.map(reservaCUDBD, ReservaR.class)).collect(Collectors.toList());
            redisReservaRCache.saveListCache(listaReservaR, vooManterDto.getCodigoVoo());
            reservaCUDRepository.saveAll(listaReservaCUDBD.get());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reservas-realizadas-voo-contaR", vooManterDto);
        }
        return vooManterDto;
    }

    public VooManterDto reverterReservasCUDRealizadasVoo(VooManterDto vooManterDto) {
        List<ReservaCUD> listaReservaCUDCache = redisReservaCUDCache.getListCache(vooManterDto.getCodigoVoo());
        if (listaReservaCUDCache != null) {
            for (ReservaCUD reservaCUD : listaReservaCUDCache) {
                reservaCUDRepository.updateReserva(reservaCUD);
            }
            redisReservaCUDCache.removeListCache(vooManterDto.getCodigoVoo());
            historicoAlteracaoEstadoReservaCUDService.reverterHistoricoEstadoReserva(listaReservaCUDCache);
        }
        return vooManterDto;
    }

}

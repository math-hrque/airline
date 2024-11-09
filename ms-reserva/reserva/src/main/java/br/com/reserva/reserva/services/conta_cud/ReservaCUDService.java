package br.com.reserva.reserva.services.conta_cud;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

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
import br.com.reserva.reserva.repositories.conta_cud.EstadoReservaCUDRepository;
import br.com.reserva.reserva.repositories.conta_cud.ReservaCUDRepository;
import br.com.reserva.reserva.utils.conta_cud.CodigoReservaGenerator;
import br.com.reserva.reserva.utils.conta_cud.RedisReservaCUDCache;

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
            throw new ReservaNaoExisteException("Reserva " + reservaCUD.getCodigoReserva() + "nao foi criada");
        }
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-cadastrada-contaR", reservaCUDCriada.get());
        ReservaManterDto reservaManterCriadaDto = mapper.map(reservaCUDCriada.get(), ReservaManterDto.class);
        return reservaManterCriadaDto;
    }

    public ReservaManterDto reverterReservaCUDcadastrada(ReservaManterDto reservaManterDto) {
        if (reservaManterDto.getCodigoReserva() != null) {
            Optional<ReservaCUD> reservaCUDBD = reservaCUDRepository.findById(reservaManterDto.getCodigoReserva());
            if (reservaCUDBD.isPresent()) {
                reservaCUDRepository.deleteById(reservaManterDto.getCodigoReserva());
            }
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

        ReservaCUD reservaCUDCache = redisReservaCUDCache.getCache(reservaCUDBD.get().getCodigoVoo());
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

        ReservaCUD reservaCUDCache = redisReservaCUDCache.getCache(reservaCUDBD.get().getCodigoVoo());
        if (reservaCUDCache == null) {
            redisReservaCUDCache.saveCache(reservaCUDBD.get());
            throw new ReservaNaoExisteException("Reserva nao existe no cache!");
        }

        reservaCUDRepository.save(reservaCUDCache);
        redisReservaCUDCache.removeCache(reservaCUDCache.getCodigoVoo());
        Optional<ReservaCUD> ReservaCUDRevertida = reservaCUDRepository.findById(reservaCUDBD.get().getCodigoVoo());
        ReservaManterDto reservaManterRevertidaDto = mapper.map(ReservaCUDRevertida.get(), ReservaManterDto.class);
        return reservaManterRevertidaDto;
    }

    public List<ReservaManterDto> cancelarReservasCUDVoo(VooManterDto vooManterDto) {
        Optional<List<ReservaCUD>> listaReservaCUDBD = reservaCUDRepository.findByCodigoVoo(vooManterDto.getCodigoVoo());
        List<ReservaCUD> listaReservaCUDAlterada = new ArrayList<>();
        List<ReservaManterDto> listaReservaManterDto = new ArrayList<>();

        if (listaReservaCUDBD.isPresent()) {
            for (ReservaCUD reservaCUD : listaReservaCUDBD.get()) {
                ReservaCUD reservaCUDCache = redisReservaCUDCache.getCache(reservaCUD.getCodigoReserva());
                if (reservaCUDCache == null) {
                    redisReservaCUDCache.saveCache(reservaCUD);
                }
                switch (reservaCUD.getEstadoReserva().getTipoEstadoReserva()) {
                    case TipoEstadoReserva.CONFIRMADO: 
                    case TipoEstadoReserva.CHECK_IN:
                    case TipoEstadoReserva.EMBARCADO:
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
            listaReservaCUDAlterada = reservaCUDRepository.saveAll(listaReservaCUDBD.get());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reservas-realizadas-contaR", listaReservaCUDBD.get());
        }
        return listaReservaManterDto; 
    }

    public void reverterReservasCUDCanceladasVoo(String codigoVoo) {
        Optional<List<ReservaCUD>> listaReservaCUDBD = reservaCUDRepository.findByCodigoVoo(codigoVoo);

        if (listaReservaCUDBD.isPresent()) {
            for (ReservaCUD reservaCUD : listaReservaCUDBD.get()) {
                ReservaCUD reservaCUDCache = redisReservaCUDCache.getCache(reservaCUD.getCodigoReserva());
                if (reservaCUDCache != null) {
                    reservaCUDRepository.save(reservaCUDCache);
                    redisReservaCUDCache.removeCache(reservaCUDCache.getCodigoReserva());
                }
            }
            // historicoAlteracaoEstadoReservaCUDService.reverterHistoricoEstadoReserva(listaReservaCUDBD.get());
        }
    }

    public VooManterDto reservasCUDRealizar(VooManterDto vooManterDto) {
        Optional<List<ReservaCUD>> listaReservaCUDBD = reservaCUDRepository.findByCodigoVoo(vooManterDto.getCodigoVoo());

        if (listaReservaCUDBD.isPresent()) {
            for (ReservaCUD reservaCUD : listaReservaCUDBD.get()) {
                ReservaCUD reservaCUDCache = redisReservaCUDCache.getCache(reservaCUD.getCodigoReserva());
                if (reservaCUDCache == null) {
                    redisReservaCUDCache.saveCache(reservaCUD);
                }
                switch (reservaCUD.getEstadoReserva().getTipoEstadoReserva()) {
                    case TipoEstadoReserva.EMBARCADO:
                        historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUD, TipoEstadoReserva.REALIZADO);
                        reservaCUD.setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.REALIZADO));
                        break;
                    case TipoEstadoReserva.CONFIRMADO: 
                    case TipoEstadoReserva.CHECK_IN:
                        historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUD, TipoEstadoReserva.NAO_REALIZADO);
                        reservaCUD.setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.NAO_REALIZADO));
                        break;
                    default:
                        break;
                }
            }
            reservaCUDRepository.saveAll(listaReservaCUDBD.get());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reservas-realizadas-contaR", listaReservaCUDBD.get());
        }
        return vooManterDto;
    }

    public VooManterDto reverterReservasCUDRealizadas(VooManterDto vooManterDto) {
        Optional<List<ReservaCUD>> listaReservaCUDBD = reservaCUDRepository.findByCodigoVoo(vooManterDto.getCodigoVoo());

        if (listaReservaCUDBD.isPresent()) {
            for (ReservaCUD reservaCUD : listaReservaCUDBD.get()) {
                ReservaCUD reservaCUDCache = redisReservaCUDCache.getCache(reservaCUD.getCodigoReserva());
                if (reservaCUDCache != null) {
                    reservaCUDRepository.save(reservaCUDCache);
                    redisReservaCUDCache.removeCache(reservaCUDCache.getCodigoReserva());
                }
            }
            // historicoAlteracaoEstadoReservaCUDService.reverterHistoricoEstadoReserva(listaReservaCUDBD.get());
        }
        return vooManterDto;
    }

    public ReservaManterDto confirmarEmbarqueReservaCUD(String codigoVoo, String codigoReserva) throws ReservaNaoExisteException, MudancaEstadoReservaInvalidaException {
        Optional<ReservaCUD> reservaCUDBD = reservaCUDRepository.findByCodigoReservaAndCodigoVoo(codigoReserva, codigoVoo);
        if (!reservaCUDBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        if (reservaCUDBD.get().getEstadoReserva().getTipoEstadoReserva() != TipoEstadoReserva.CHECK_IN) {
            throw new MudancaEstadoReservaInvalidaException("Estado de Reserva nao eh valido para ser Embarcado!");
        }
        historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUDBD.get(), TipoEstadoReserva.EMBARCADO);
        reservaCUDBD.get().setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.EMBARCADO));
        ReservaCUD reservaEmbarcadaCUD = reservaCUDRepository.save(reservaCUDBD.get());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-confirmar-embarque-contaR", reservaEmbarcadaCUD);
        ReservaManterDto reservaManterEmbarcadaCUD = mapper.map(reservaEmbarcadaCUD, ReservaManterDto.class);
        return reservaManterEmbarcadaCUD;
    }

    public ReservaManterDto fazerCheckinReservaCUD(String codigoReserva) throws ReservaNaoExisteException, MudancaEstadoReservaInvalidaException {
        Optional<ReservaCUD> reservaCUDBD = reservaCUDRepository.findById(codigoReserva);
        if (!reservaCUDBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        if (reservaCUDBD.get().getEstadoReserva().getTipoEstadoReserva() != TipoEstadoReserva.CONFIRMADO) {
            throw new MudancaEstadoReservaInvalidaException("Estado de Reserva nao eh valido para ser Check-in!");
        }
        historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUDBD.get(), TipoEstadoReserva.CHECK_IN);
        reservaCUDBD.get().setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.CHECK_IN));
        ReservaCUD reservaCheckinCUD = reservaCUDRepository.save(reservaCUDBD.get());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-fazer-checkin-contaR", reservaCheckinCUD);
        ReservaManterDto reservaManterCheckinCUD = mapper.map(reservaCheckinCUD, ReservaManterDto.class);
        return reservaManterCheckinCUD;
    }
}

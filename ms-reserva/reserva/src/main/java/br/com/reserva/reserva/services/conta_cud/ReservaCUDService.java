package br.com.reserva.reserva.services.conta_cud;

import java.util.List;
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
import br.com.reserva.reserva.utils.conta_cud.RedisReservaCUDCache;
import net.bytebuddy.implementation.bytecode.Throw;

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

    public VooManterDto realizarReservasCUD(VooManterDto vooManterDto) throws Exception {
        Optional<List<ReservaCUD>> listaReservaCUDBD = reservaCUDRepository.findByCodigoVoo(vooManterDto.getCodigoVoo());

        if (listaReservaCUDBD.isPresent()) {
            for (ReservaCUD reservaCUD : listaReservaCUDBD.get()) {
                ReservaCUD reservaCUDCache = redisReservaCUDCache.getCache(reservaCUD.getCodigoReserva());
                if (reservaCUDCache == null) {
                    redisReservaCUDCache.saveCache(reservaCUD);
                }
                switch (reservaCUD.getEstadoReserva().getTipoEstadoReserva()) {
                    case TipoEstadoReserva.EMBARCADO:
                        // historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUD, TipoEstadoReserva.REALIZADO);
                        reservaCUD.setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.REALIZADO));
                        break;
                    case TipoEstadoReserva.CONFIRMADO: 
                    case TipoEstadoReserva.CHECK_IN:
                        // historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUD, TipoEstadoReserva.NAO_REALIZADO);
                        reservaCUD.setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.NAO_REALIZADO));
                        break;
                    default:
                        break;
                }
            }
            reservaCUDRepository.saveAll(listaReservaCUDBD.get());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reservas-realizadas-contaR", listaReservaCUDBD.get());
        }
        // throw new Exception("Erro ao realizar reserva");
        return vooManterDto;
    }

    public VooManterDto reverterReservasRealizadasCUD(VooManterDto vooManterDto) {
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

    public ReservaManterDto confirmarEmbarqueCUD(String codigoVoo, String codigoReserva) throws ReservaNaoExisteException, MudancaEstadoReservaInvalidaException {
        Optional<ReservaCUD> reservaCUDBD = reservaCUDRepository.findByCodigoReservaAndCodigoVoo(codigoReserva, codigoVoo);
        if (!reservaCUDBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        if (reservaCUDBD.get().getEstadoReserva().getTipoEstadoReserva() != TipoEstadoReserva.CHECK_IN) {
            throw new MudancaEstadoReservaInvalidaException("Estado de Reserva nao eh valido para ser Embarcado!");
        }
        // historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUDBD.get(), TipoEstadoReserva.EMBARCADO);
        reservaCUDBD.get().setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.EMBARCADO));
        ReservaCUD reservaEmbarcadaCUD = reservaCUDRepository.save(reservaCUDBD.get());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-confirmar-embarque-contaR", reservaEmbarcadaCUD);
        ReservaManterDto reservaManterEmbarcadaCUD = mapper.map(reservaEmbarcadaCUD, ReservaManterDto.class);
        return reservaManterEmbarcadaCUD;
    }

    public ReservaManterDto fazerCheckin(String codigoReserva) throws ReservaNaoExisteException, MudancaEstadoReservaInvalidaException {
        Optional<ReservaCUD> reservaCUDBD = reservaCUDRepository.findById(codigoReserva);
        if (!reservaCUDBD.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        if (reservaCUDBD.get().getEstadoReserva().getTipoEstadoReserva() != TipoEstadoReserva.CONFIRMADO) {
            throw new MudancaEstadoReservaInvalidaException("Estado de Reserva nao eh valido para ser Check-in!");
        }
        // historicoAlteracaoEstadoReservaCUDService.alteraHistoricoEstadoReserva(reservaCUDBD.get(), TipoEstadoReserva.CHECK_IN);
        reservaCUDBD.get().setEstadoReserva(estadoReservaCUDRepository.findByTipoEstadoReserva(TipoEstadoReserva.CHECK_IN));
        ReservaCUD reservaCheckinCUD = reservaCUDRepository.save(reservaCUDBD.get());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "ms-reserva-reserva-fazer-checkin-contaR", reservaCheckinCUD);
        ReservaManterDto reservaManterCheckinCUD = mapper.map(reservaCheckinCUD, ReservaManterDto.class);
        return reservaManterCheckinCUD;
    }
}

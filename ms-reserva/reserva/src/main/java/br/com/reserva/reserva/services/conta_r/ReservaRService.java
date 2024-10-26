package br.com.reserva.reserva.services.conta_r;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.reserva.reserva.dtos.ReservaManterDto;
import br.com.reserva.reserva.dtos.VooManterDto;
import br.com.reserva.reserva.enums.TipoEstadoReserva;
import br.com.reserva.reserva.exeptions.MudancaEstadoReservaInvalidaException;
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

    public List<ReservaManterDto> realizarReservasR(List<ReservaCUD> listaReservaCUD) {
        List<ReservaR> listaReservaR = new ArrayList<>();
        List<ReservaManterDto> listaReservaManterDto = new ArrayList<>();
        for (ReservaCUD reservaCUD : listaReservaCUD) {
            Optional<ReservaR> reservaR = reservaRRepository.findById(reservaCUD.getCodigoReserva());
            if (reservaR.isPresent()) {
                reservaR.get().setSiglaEstadoReserva(reservaCUD.getEstadoReserva().getSiglaEstadoReserva());
                reservaR.get().setTipoEstadoReserva(reservaCUD.getEstadoReserva().getTipoEstadoReserva());
                listaReservaR.add(reservaR.get());
                ReservaManterDto reservaManterRealizadaR = mapper.map(reservaR.get(), ReservaManterDto.class);
                listaReservaManterDto.add(reservaManterRealizadaR);
            }
        }
        reservaRRepository.saveAll(listaReservaR);
        return listaReservaManterDto;
    }

    public ReservaManterDto confirmarEmbarqueR(ReservaCUD reservaCUD) throws ReservaNaoExisteException {
        Optional<ReservaR> reservaR = reservaRRepository.findById(reservaCUD.getCodigoReserva());
        if (!reservaR.isPresent()) {
            throw new ReservaNaoExisteException("Reserva nao existe");
        }

        reservaR.get().setSiglaEstadoReserva(reservaCUD.getEstadoReserva().getSiglaEstadoReserva());
        reservaR.get().setTipoEstadoReserva(reservaCUD.getEstadoReserva().getTipoEstadoReserva());
        ReservaR reservaEmbarcadaR = reservaRRepository.save(reservaR.get());
        ReservaManterDto reservaManterEmbarcadaR = mapper.map(reservaEmbarcadaR, ReservaManterDto.class);
        return reservaManterEmbarcadaR;
    }
}

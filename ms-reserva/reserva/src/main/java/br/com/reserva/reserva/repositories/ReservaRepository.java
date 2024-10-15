package br.com.reserva.reserva.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.reserva.reserva.enums.TipoEstadoReserva;
import br.com.reserva.reserva.models.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, String> {
    Optional<List<Reserva>> findByCodigoVoo(String codigoVoo);
    Optional<List<Reserva>> findByCodigoVooAndEstadoReservaTipoEstadoReservaNot(String codigoVoo, TipoEstadoReserva tipoEstadoReserva);
    Optional<List<Reserva>> findByIdCliente(Long idCliente);
    Optional<List<Reserva>> findByIdClienteAndEstadoReservaTipoEstadoReserva(Long idCliente, TipoEstadoReserva tipoEstadoReserva);
}

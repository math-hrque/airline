package br.com.reserva.reserva.repositories.conta_cud;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.reserva.reserva.enums.TipoEstadoReserva;
import br.com.reserva.reserva.models.conta_cud.ReservaCUD;

@Repository
public interface ReservaCUDRepository extends JpaRepository<ReservaCUD, String> {
    Optional<List<ReservaCUD>> findByCodigoVoo(String codigoVoo);
    Optional<List<ReservaCUD>> findByCodigoVooAndEstadoReservaTipoEstadoReservaNot(String codigoVoo, TipoEstadoReserva tipoEstadoReserva);
    Optional<List<ReservaCUD>> findByIdCliente(Long idCliente);
    Optional<List<ReservaCUD>> findByIdClienteAndEstadoReservaTipoEstadoReserva(Long idCliente, TipoEstadoReserva tipoEstadoReserva);
}

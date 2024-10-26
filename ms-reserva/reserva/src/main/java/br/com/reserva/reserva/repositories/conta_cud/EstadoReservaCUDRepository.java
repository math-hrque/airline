package br.com.reserva.reserva.repositories.conta_cud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.reserva.reserva.enums.TipoEstadoReserva;
import br.com.reserva.reserva.models.conta_cud.EstadoReservaCUD;

@Repository
public interface EstadoReservaCUDRepository extends JpaRepository<EstadoReservaCUD, Long> {
    EstadoReservaCUD findByTipoEstadoReserva(TipoEstadoReserva tipoEstadoReserva);
}

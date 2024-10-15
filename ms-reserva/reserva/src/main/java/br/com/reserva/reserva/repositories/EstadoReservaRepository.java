package br.com.reserva.reserva.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.reserva.reserva.enums.TipoEstadoReserva;
import br.com.reserva.reserva.models.EstadoReserva;

@Repository
public interface EstadoReservaRepository extends JpaRepository<EstadoReserva, Long> {
    Optional<EstadoReserva> findByTipoEstadoReserva(TipoEstadoReserva tipoEstadoReserva);
}

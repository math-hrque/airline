package br.com.reserva.reserva.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.reserva.reserva.models.HistoricoAlteracaoEstadoReserva;

@Repository
public interface HistoricoAlteracaoEstadoReservaRepository extends JpaRepository<HistoricoAlteracaoEstadoReserva, Long> {

}

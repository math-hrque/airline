package br.com.reserva.reserva.repositories.conta_r;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.reserva.reserva.models.conta_r.HistoricoAlteracaoEstadoReservaR;

@Repository
public interface HistoricoAlteracaoEstadoReservaRRepository extends JpaRepository<HistoricoAlteracaoEstadoReservaR, Long> {

}

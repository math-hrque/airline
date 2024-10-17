package br.com.reserva.reserva.repositories.conta_cud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.reserva.reserva.models.conta_cud.HistoricoAlteracaoEstadoReservaCUD;

@Repository
public interface HistoricoAlteracaoEstadoReservaCUDRepository extends JpaRepository<HistoricoAlteracaoEstadoReservaCUD, Long> {

}

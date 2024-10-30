package br.com.reserva.reserva.repositories.conta_cud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import br.com.reserva.reserva.models.conta_cud.HistoricoAlteracaoEstadoReservaCUD;
import br.com.reserva.reserva.models.conta_cud.ReservaCUD;
import jakarta.transaction.Transactional;

@Repository
public interface HistoricoAlteracaoEstadoReservaCUDRepository extends JpaRepository<HistoricoAlteracaoEstadoReservaCUD, Long> {
    Optional<HistoricoAlteracaoEstadoReservaCUD> findByReservaCodigoReserva(String codigoReserva);
    void deleteByReservaCodigoReserva(String codigoReserva);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO historico_alteracao_estado_reserva (data_alteracao_estado_reserva, codigo_reserva, id_estado_reserva_origem, id_estado_reserva_destino) " +
                   "VALUES (:#{#historico.dataAlteracaoEstadoReserva}, :#{#historico.reserva.codigoReserva}, :#{#historico.estadoReservaOrigem.idEstadoReserva}, :#{#historico.estadoReservaDestino.idEstadoReserva})", 
                   nativeQuery = true)
    void saveHistorico(@Param("historico") HistoricoAlteracaoEstadoReservaCUD historico);
}

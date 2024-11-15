package br.com.reserva.reserva.repositories.conta_cud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import br.com.reserva.reserva.models.conta_cud.HistoricoAlteracaoEstadoReservaCUD;
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
    @Modifying
    @Transactional
    @Query(value = "UPDATE historico_alteracao_estado_reserva " +
                "SET data_alteracao_estado_reserva = :#{#historico.dataAlteracaoEstadoReserva}, " +
                "id_estado_reserva_origem = :#{#historico.estadoReservaOrigem.idEstadoReserva}, " +
                "id_estado_reserva_destino = :#{#historico.estadoReservaDestino.idEstadoReserva} " +
                "WHERE codigo_reserva = :#{#historico.reserva.codigoReserva}", 
                nativeQuery = true)
    void updateHistorico(@Param("historico") HistoricoAlteracaoEstadoReservaCUD historico);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM historico_alteracao_estado_reserva WHERE codigo_reserva = :codigoReserva", nativeQuery = true)
    void deleteByCodigoReserva(@Param("codigoReserva") String codigoReserva);
}

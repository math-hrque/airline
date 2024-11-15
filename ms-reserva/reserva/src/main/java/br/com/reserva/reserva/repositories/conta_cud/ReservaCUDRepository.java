package br.com.reserva.reserva.repositories.conta_cud;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.reserva.reserva.enums.TipoEstadoReserva;
import br.com.reserva.reserva.models.conta_cud.ReservaCUD;
import jakarta.transaction.Transactional;

@Repository
public interface ReservaCUDRepository extends JpaRepository<ReservaCUD, String> {
    boolean existsByCodigoReserva(String codigoReserva);
    Optional<ReservaCUD> findByCodigoReservaAndCodigoVoo(String codigoReserva, String codigoVoo);
    Optional<List<ReservaCUD>> findByCodigoVoo(String codigoVoo);
    Optional<List<ReservaCUD>> findByCodigoVooAndEstadoReservaTipoEstadoReservaNot(String codigoVoo, TipoEstadoReserva tipoEstadoReserva);
    Optional<List<ReservaCUD>> findByIdCliente(Long idCliente);
    Optional<List<ReservaCUD>> findByIdClienteAndEstadoReservaTipoEstadoReserva(Long idCliente, TipoEstadoReserva tipoEstadoReserva);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO reserva (codigo_reserva, codigo_voo, data_reserva, valor_reserva, milhas_utilizadas, quantidade_poltronas, id_cliente, id_estado_reserva) " +
                   "VALUES (:#{#reservaCUD.codigoReserva}, :#{#reservaCUD.codigoVoo}, CURRENT_TIMESTAMP, :#{#reservaCUD.valorReserva}, :#{#reservaCUD.milhasUtilizadas}, " +
                   ":#{#reservaCUD.quantidadePoltronas}, :#{#reservaCUD.idCliente}, :#{#reservaCUD.estadoReserva.idEstadoReserva})", nativeQuery = true)
    void saveReserva(@Param("reservaCUD") ReservaCUD reservaCUD);

    @Modifying
    @Transactional
    @Query(value = "UPDATE reserva SET codigo_voo = :#{#reservaCUD.codigoVoo}, " +
                   "data_reserva = :#{#reservaCUD.dataReserva}, " +
                   "valor_reserva = :#{#reservaCUD.valorReserva}, " +
                   "milhas_utilizadas = :#{#reservaCUD.milhasUtilizadas}, " +
                   "quantidade_poltronas = :#{#reservaCUD.quantidadePoltronas}, " +
                   "id_cliente = :#{#reservaCUD.idCliente}, " +
                   "id_estado_reserva = :#{#reservaCUD.estadoReserva.idEstadoReserva} " +
                   "WHERE codigo_reserva = :#{#reservaCUD.codigoReserva}", 
           nativeQuery = true)
    void updateReserva(@Param("reservaCUD") ReservaCUD reservaCUD);    
}

package br.com.voos.voos.repositories;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.voos.voos.enums.TipoEstadoVoo;
import br.com.voos.voos.models.Voo;
import jakarta.transaction.Transactional;

@Repository
public interface VoosRepository extends JpaRepository<Voo, String> {
    @Query("SELECT v FROM Voo v " +
    "WHERE v.dataVoo >= :dataAtual " +
    "AND v.estadoVoo.tipoEstadoVoo = 'CONFIRMADO' " +
    "AND (:codigoAeroportoOrigem IS NULL OR v.aeroportoOrigem.codigoAeroporto = :codigoAeroportoOrigem) " +
    "AND (:codigoAeroportoeroportoDestino IS NULL OR v.aeroportoDestino.codigoAeroporto = :codigoAeroportoeroportoDestino)")
    Optional<List<Voo>> findVoosConfirmadosByDataVooAndAeroportos(OffsetDateTime dataAtual, String codigoAeroportoOrigem, String codigoAeroportoeroportoDestino);

    @Query("SELECT v FROM Voo v " +
    "WHERE v.dataVoo BETWEEN :dataAtual AND :dataLimite " +
    "AND v.codigoVoo = :codigoVoo " +
    "AND v.estadoVoo.tipoEstadoVoo = 'CONFIRMADO'")
    Optional<Voo> findVooConfirmadoByCodigoVooNasProximas48Horas(OffsetDateTime dataAtual, OffsetDateTime dataLimite, String codigoVoo);

    @Query("SELECT v FROM Voo v " +
    "WHERE v.dataVoo BETWEEN :dataAtual AND :dataLimite " +
    "AND v.estadoVoo.tipoEstadoVoo = 'CONFIRMADO'")
    Optional<List<Voo>> findVoosConfirmadosByProximas48Horas(OffsetDateTime dataAtual, OffsetDateTime dataLimite);
    Optional<Voo> findByCodigoVooAndEstadoVooTipoEstadoVoo(String codigoVoo, TipoEstadoVoo tipoEstadoVoo);

    @Modifying
    @Transactional
    @Query("UPDATE Voo v SET " +
           "v.quantidadePoltronasOcupadas = :#{#voo.quantidadePoltronasOcupadas} " +
           "WHERE v.codigoVoo = :#{#voo.codigoVoo}")
    void atualizarPoltronasOcupadasVoo(@Param("voo") Voo voo);
    
}

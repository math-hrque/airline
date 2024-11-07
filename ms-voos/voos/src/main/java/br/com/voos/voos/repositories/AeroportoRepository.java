package br.com.voos.voos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.voos.voos.models.Aeroporto;
import java.util.Optional;

@Repository
public interface AeroportoRepository extends JpaRepository<Aeroporto, String> {

    @Query("SELECT a FROM Aeroporto a " +
            "WHERE a.codigoAeroporto = :codigoAeroporto ")
    Optional<Aeroporto> findAeroportoByCodigo(String codigoAeroporto);

}

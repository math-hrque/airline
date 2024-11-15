package br.com.cliente.cliente.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cliente.cliente.models.Milhas;
import jakarta.transaction.Transactional;

@Repository
public interface MilhasRepository extends JpaRepository<Milhas, Long> {
    Optional<List<Milhas>> findByClienteIdCliente(Long idCliente);
    Optional<Milhas> findByCodigoReserva(String codigoReserva);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO milhas (data_transacao, valor_reais, quantidade_milhas, descricao, codigo_reserva, id_cliente, id_transacao) " +
                   "VALUES (CURRENT_TIMESTAMP, :#{#milhas.valorReais}, :#{#milhas.quantidadeMilhas}, :#{#milhas.descricao}, " +
                   ":#{#milhas.codigoReserva}, :#{#milhas.cliente.idCliente}, :#{#milhas.transacao.idTransacao})", nativeQuery = true)
    void saveMilhas(@Param("milhas") Milhas milhas);
}

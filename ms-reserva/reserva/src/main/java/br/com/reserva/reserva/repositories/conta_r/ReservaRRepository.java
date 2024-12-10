package br.com.reserva.reserva.repositories.conta_r;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.reserva.reserva.enums.TipoEstadoReserva;
import br.com.reserva.reserva.models.conta_r.ReservaR;

@Repository
public interface ReservaRRepository extends JpaRepository<ReservaR, String> {
    Optional<List<ReservaR>> findByCodigoVoo(String codigoVoo);
    Optional<List<ReservaR>> findByCodigoVooInAndIdCliente(List<String> listaCodigoVoo, Long idCliente);
    Optional<List<ReservaR>> findByCodigoVooInAndIdClienteAndTipoEstadoReserva(List<String> listaCodigoVoo, Long idCliente, TipoEstadoReserva tipoEstadoReserva);
    Optional<List<ReservaR>> findByCodigoVooAndTipoEstadoReservaNot(String codigoVoo, TipoEstadoReserva tipoEstadoReserva);
    Optional<List<ReservaR>> findByIdCliente(Long idCliente);
    Optional<List<ReservaR>> findByIdClienteAndTipoEstadoReserva(Long idCliente, TipoEstadoReserva tipoEstadoReserva);
    Optional<ReservaR> findByCodigoReserva(String codigoReserva);
}

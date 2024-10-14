package br.com.reserva.reserva.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.reserva.reserva.models.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, String> {

}

package br.com.cliente.cliente.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cliente.cliente.models.Milhas;

@Repository
public interface MilhasRepository extends JpaRepository<Milhas, Long> {

}

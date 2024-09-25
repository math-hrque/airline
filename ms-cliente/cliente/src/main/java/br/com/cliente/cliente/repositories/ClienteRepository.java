package br.com.cliente.cliente.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cliente.cliente.models.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}

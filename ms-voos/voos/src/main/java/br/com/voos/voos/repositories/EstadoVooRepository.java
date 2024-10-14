package br.com.voos.voos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.voos.voos.models.EstadoVoo;

@Repository
public interface EstadoVooRepository extends JpaRepository<EstadoVoo, Long> {

}

package br.com.voos.voos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.voos.voos.models.Aeroporto;

@Repository
public interface AeroportoRepository extends JpaRepository<Aeroporto, String> {

}

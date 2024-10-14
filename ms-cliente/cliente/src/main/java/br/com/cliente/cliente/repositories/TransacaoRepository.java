package br.com.cliente.cliente.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cliente.cliente.models.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

}

package br.com.funcionario.funcionario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.funcionario.funcionario.models.Funcionario;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByEmail(String email);
    Optional<List<Funcionario>> findByAtivo(boolean ativo);
    Optional<Funcionario> findByIdFuncionarioAndAtivo(Long idFuncionario, boolean ativo);
    Optional<Funcionario> findByEmailAndAtivo(String email, boolean ativo);
    Optional<List<Funcionario>> findByCpfOrEmail(String cpf, String email);
}

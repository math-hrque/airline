package br.com.auth.auth.repositories;

import br.com.auth.auth.models.Usuario;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String>{
    Optional<Usuario> findByEmail(String email);
    Usuario findByEmailAndAtivo(String email, boolean ativo);
}

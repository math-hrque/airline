package br.com.auth.auth.repositories;

import br.com.auth.auth.models.Usuario;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String>{
    
}

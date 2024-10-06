package br.com.auth.auth.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.auth.auth.enums.Tipo;
import br.com.auth.auth.models.Usuario;

import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDBInitializer {

    @Bean
    CommandLineRunner initDatabase(MongoTemplate mongoTemplate, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            if (!mongoTemplate.collectionExists(Usuario.class)) {
                mongoTemplate.createCollection(Usuario.class);
            }

            long count = mongoTemplate.count(new org.springframework.data.mongodb.core.query.Query(), Usuario.class);
            if (count == 0) {
                String senha = "pass";
                String senhaCriptografada1 = passwordEncoder.encode(senha);
                String senhaCriptografada2 = passwordEncoder.encode(senha);
                String senhaCriptografada3 = passwordEncoder.encode(senha);
                String senhaCriptografada4 = passwordEncoder.encode(senha);
                String senhaCriptografada5 = passwordEncoder.encode(senha);
                String senhaCriptografada6 = passwordEncoder.encode(senha);
                String senhaCriptografada7 = passwordEncoder.encode(senha);
                String senhaCriptografada8 = passwordEncoder.encode(senha);
                String senhaCriptografada9 = passwordEncoder.encode(senha);
                String senhaCriptografada10 = passwordEncoder.encode(senha);
                mongoTemplate.insert(new Usuario(null, "matheus@gmail.com", senhaCriptografada1, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario(null, "maria@gmail.com", senhaCriptografada2, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario(null, "murilo@gmail.com", senhaCriptografada3, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario(null, "michele@gmail.com", senhaCriptografada4, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario(null, "michael@gmail.com", senhaCriptografada5, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario(null, "maira@gmail.com", senhaCriptografada6, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario(null, "marcelo@gmail.com", senhaCriptografada7, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario(null, "muriel@gmail.com", senhaCriptografada8, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario(null, "michaela@gmail.com", senhaCriptografada9, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario(null, "mauro@gmail.com", senhaCriptografada10, Tipo.FUNCIONARIO, true));
            }
        };
    }
}

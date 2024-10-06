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
                mongoTemplate.insert(new Usuario("matheus@gmail.com", senhaCriptografada1, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario("maria@gmail.com", senhaCriptografada2, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario("murilo@gmail.com", senhaCriptografada3, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario("michele@gmail.com", senhaCriptografada4, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario("michael@gmail.com", senhaCriptografada5, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario("maira@gmail.com", senhaCriptografada6, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario("marcelo@gmail.com", senhaCriptografada7, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario("muriel@gmail.com", senhaCriptografada8, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario("michaela@gmail.com", senhaCriptografada9, Tipo.FUNCIONARIO, true));
                mongoTemplate.insert(new Usuario("mauro@gmail.com", senhaCriptografada10, Tipo.FUNCIONARIO, true));
            }
        };
    }
}

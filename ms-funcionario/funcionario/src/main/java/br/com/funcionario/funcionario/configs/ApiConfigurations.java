package br.com.funcionario.funcionario.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfigurations {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}

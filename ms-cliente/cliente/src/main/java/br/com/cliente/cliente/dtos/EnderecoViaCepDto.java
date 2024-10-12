package br.com.cliente.cliente.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoViaCepDto {
    @JsonProperty("cep")
    private String cep;
    
    @JsonProperty("estado")
    private String estado;

    @JsonProperty("localidade")
    private String cidade;

    @JsonProperty("bairro")
    private String bairro;

    @JsonProperty("logradouro")
    private String rua;

    private String numero;
    
    private String complemento = "";
}

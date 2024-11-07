package br.com.voos.voos.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarVooDto {
    private String codigoVoo;
    private String dataHora;
    private Double valorPassagemReais;
    private Integer quantidadePoltronasTotal;
    private String aeroportoOrigem;
    private String aeroportoDestino;
}

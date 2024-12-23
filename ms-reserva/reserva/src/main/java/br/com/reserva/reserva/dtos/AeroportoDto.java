package br.com.reserva.reserva.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class AeroportoDto {
    private String codigoAeroporto;
    private String nome;
    private String cidade;
    private String estado;
}

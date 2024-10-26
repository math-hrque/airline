package br.com.voos.voos.dtos;

import java.time.OffsetDateTime;

import br.com.voos.voos.enums.TipoEstadoVoo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class VooDto {
    private String codigoVoo;
    private OffsetDateTime dataVoo;
    private Double valorPassagem;
    private Integer quantidadePoltronasTotal;
    private Integer quantidadePoltronasOcupadas;
    private TipoEstadoVoo estadoVoo;
    private AeroportoDto aeroportoOrigem;
    private AeroportoDto aeroportoDestino;
}

package br.com.reserva.reserva.dtos;

import java.time.OffsetDateTime;

import br.com.reserva.reserva.enums.TipoEstadoVoo;
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

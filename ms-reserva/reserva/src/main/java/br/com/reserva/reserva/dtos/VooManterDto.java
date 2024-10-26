package br.com.reserva.reserva.dtos;

import java.time.OffsetDateTime;

import br.com.reserva.reserva.enums.TipoEstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class VooManterDto {
    private String codigoVoo;
    private OffsetDateTime dataVoo;
    private Double valorPassagem;
    private Integer quantidadePoltronasTotal;
    private Integer quantidadePoltronasOcupadas;
    private TipoEstadoReserva estadoVoo;
    private String codigoAeroportoOrigem;
    private String codigoAeroportoDestino;
} 

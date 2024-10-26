package br.com.saga.saga.saga.dtos;

import java.time.OffsetDateTime;

import br.com.saga.saga.saga.enums.SiglaEstadoReserva;
import br.com.saga.saga.saga.enums.TipoEstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaManterDto {
    private String codigoReserva;
    private OffsetDateTime dataReserva;
    private Double valorReserva;
    private Integer milhasUtilizadas;
    private Integer quantidadePoltronas;
    private Long idCliente;
    private SiglaEstadoReserva siglaEstadoReserva;
    private TipoEstadoReserva tipoEstadoReserva;
    private String codigoVoo;
    private String codigoAeroportoOrigem;
    private String codigoAeroportoDestino;
}

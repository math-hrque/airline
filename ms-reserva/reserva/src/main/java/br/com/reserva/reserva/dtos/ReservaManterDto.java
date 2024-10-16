package br.com.reserva.reserva.dtos;

import java.time.LocalDateTime;

import br.com.reserva.reserva.enums.SiglaEstadoReserva;
import br.com.reserva.reserva.enums.TipoEstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaManterDto {
    private String codigoReserva;
    private LocalDateTime dataReserva;
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

package br.com.voos.voos.dtos;

import java.time.OffsetDateTime;

import br.com.voos.voos.enums.SiglaEstadoReserva;
import br.com.voos.voos.enums.TipoEstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDto {
    private String codigoReserva;
    private OffsetDateTime dataReserva;
    private Double valorReserva;
    private Integer milhasUtilizadas;
    private Integer quantidadePoltronas;
    private Long idCliente;
    private SiglaEstadoReserva siglaEstadoReserva;
    private TipoEstadoReserva tipoEstadoReserva;
    private VooDto voo;
}

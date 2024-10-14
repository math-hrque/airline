package br.com.cliente.cliente.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteMilhasDto {
    private Long idCliente;
    private Integer saldoMilhas;
    private List<MilhasDto> listaMilhas;
}

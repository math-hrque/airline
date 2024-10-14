package br.com.cliente.cliente.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestCadastrarDto {
    private String email;
    private String senha = "";
    private String tipo = "CLIENTE";
}

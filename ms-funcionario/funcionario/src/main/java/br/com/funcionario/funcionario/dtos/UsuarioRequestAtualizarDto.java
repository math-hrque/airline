package br.com.funcionario.funcionario.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestAtualizarDto {
    private Long id;
    private String oldEmail;
    private String email;
    private String senha = "";
    private String tipo = "FUNCIONARIO";
}

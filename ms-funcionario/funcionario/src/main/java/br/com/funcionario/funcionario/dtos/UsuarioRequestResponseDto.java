package br.com.funcionario.funcionario.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestResponseDto {
    private String email;
    private String errorMessage;
}

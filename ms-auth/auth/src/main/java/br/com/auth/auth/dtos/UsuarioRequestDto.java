package br.com.auth.auth.dtos;

import jakarta.validation.constraints.*;

public record UsuarioRequestDto(
    @Email(message = "Email inválido")
    @Size(min = 9, max = 100, message = "Email deve ter entre 9 e 100 caracteres")
    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "Email deve conter apenas letras minúsculas, números, sublinhados (_), hífens (-), e pontos (.)")
    String email,

    @NotBlank(message = "senha é obrigatório")
    String senha
) {

}

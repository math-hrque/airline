package br.com.saga.saga.saga.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDto {
    @NotBlank(message = "CEP eh obrigatorio")
    @Pattern(regexp = "\\d{8}", message = "CEP deve conter exatamente 8 digitos")
    private String cep;
    
    @NotBlank(message = "Estado eh obrigatorio")
    @Pattern(regexp = "^[a-zA-Z\\u00C0-\\u00FF\\s0-9]+$", message = "Estado invalido")
    @Size(min = 2, max = 50, message = "Estado deve ter entre 2 e 50 caracteres")
    private String estado;

    @NotBlank(message = "Cidade eh obrigatorio")
    @Pattern(regexp = "^[a-zA-Z\\u00C0-\\u00FF\\s0-9]+$", message = "Cidade invalido")
    @Size(min = 2, max = 50, message = "Cidade deve ter entre 2 e 50 caracteres")
    private String cidade;

    @NotBlank(message = "Bairro eh obrigatorio")
    @Pattern(regexp = "^[a-zA-Z\\u00C0-\\u00FF\\s0-9]+$", message = "Bairro invalido")
    @Size(min = 2, max = 50, message = "Bairro deve ter entre 2 e 50 caracteres")
    private String bairro;

    @NotBlank(message = "Rua eh obrigatorio")
    @Pattern(regexp = "^[a-zA-Z\\u00C0-\\u00FF\\s0-9]+$", message = "Rua invalido")
    @Size(min = 2, max = 100, message = "Rua deve ter entre 2 e 100 caracteres")
    private String rua;

    @NotBlank(message = "Numero eh obrigatorio")
    @Pattern(regexp = "^[a-zA-Z\\u00C0-\\u00FF\\s0-9]+$", message = "Numero invalido")
    @Size(min = 2, max = 50, message = "Rua deve ter entre 2 e 50 caracteres")
    private String numero;
    
    @Pattern(regexp = "^[a-zA-Z\\u00C0-\\u00FF\\s0-9]+$", message = "Complemento invalido")
    @Size(min = 2, max = 100, message = "Complemento deve ter entre 2 e 100 caracteres")
    private String complemento = "";
}

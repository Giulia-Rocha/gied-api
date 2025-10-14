package com.fiap.giedapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record UserRequestDTO (
        @NotEmpty
        String nome,
        @NotBlank
        String login,
        @NotBlank
        String senha,
        @NotBlank @Pattern(regexp = "admin|default", message = "tipo de " +
                "usuário inválido")
        String tipoUsuario

){
}

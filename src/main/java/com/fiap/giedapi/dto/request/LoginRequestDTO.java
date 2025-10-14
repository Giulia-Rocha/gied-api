package com.fiap.giedapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDTO(
        @NotBlank
        String login,
        @NotBlank
        String senha
) {
}

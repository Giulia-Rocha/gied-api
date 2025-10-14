package com.fiap.giedapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SenhaRequestDTO (
        @NotBlank
        String senhaAtual,
        @NotBlank
        String senhaNova
){
}

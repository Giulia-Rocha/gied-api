package com.fiap.giedapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemSaidaDTO(
        @NotNull
        Long id,
        @NotNull @Positive
        Integer quantidade
) {
}

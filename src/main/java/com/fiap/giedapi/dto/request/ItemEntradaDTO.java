package com.fiap.giedapi.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ItemEntradaDTO(
        @NotNull
        Long id,
      @Positive
      @NotNull
      Integer quantidade,
      @NotBlank
        String descricao,
      @NotBlank
      String numeroLote,
      @Future(message = "A data deve ser futura")
      @NotNull
      @JsonFormat(pattern = "dd/MM/yyyy")
      LocalDate dataValidade
) {
}

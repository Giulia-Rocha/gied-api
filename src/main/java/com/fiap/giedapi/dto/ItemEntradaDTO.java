package com.fiap.giedapi.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ItemEntradaDTO(
      Long id,
      Integer quantidade,
      String numeroLote,
      @JsonFormat(pattern = "dd/MM/yyyy")
      LocalDate dataValidade
) {
}

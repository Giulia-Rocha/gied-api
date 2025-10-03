package com.fiap.giedapi.dto;

public record ItemResponseDTO(
        Long id,
        String nome,
        Integer estoqueTotal
) {
}

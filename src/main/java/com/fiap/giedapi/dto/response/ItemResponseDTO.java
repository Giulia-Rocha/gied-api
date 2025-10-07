package com.fiap.giedapi.dto.response;

public record ItemResponseDTO(
        Long id,
        String nome,
        Integer estoqueTotal
) {
}

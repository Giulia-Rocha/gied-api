package com.fiap.giedapi.dto.response;

import com.fiap.giedapi.domain.model.Item;

import java.util.List;

public record ItemEstoqueBaixoDTO(
        Long id,
        String nome,
        Integer nivelMinimo,
        Integer quantidadeNoEstoque

) {
}

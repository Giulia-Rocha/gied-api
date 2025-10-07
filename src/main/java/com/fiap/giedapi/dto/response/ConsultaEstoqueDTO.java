package com.fiap.giedapi.dto.response;

import com.fiap.giedapi.domain.model.Item;
import com.fiap.giedapi.domain.model.LoteEstoque;

import java.util.List;

public record ConsultaEstoqueDTO(Item item, List<LoteEstoque> lotes) {
}

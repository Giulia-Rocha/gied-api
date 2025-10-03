package com.fiap.giedapi.dto;

import com.fiap.giedapi.domain.model.Item;
import com.fiap.giedapi.domain.model.LoteEstoque;

import java.util.List;

public record ConsultaEstoqueDTO(Item item, List<LoteEstoque> lotes) {
}

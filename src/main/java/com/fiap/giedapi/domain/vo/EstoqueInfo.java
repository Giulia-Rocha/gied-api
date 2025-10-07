package com.fiap.giedapi.domain.vo;

import com.fiap.giedapi.domain.model.Item;
import com.fiap.giedapi.domain.model.LoteEstoque;

import java.util.List;

public record EstoqueInfo (
        Item item,
        List<LoteEstoque> lotes
){
}

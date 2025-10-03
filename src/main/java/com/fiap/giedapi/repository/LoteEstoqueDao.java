package com.fiap.giedapi.repository;

import com.fiap.giedapi.domain.model.LoteEstoque;

import java.util.List;
import java.util.Optional;

public interface LoteEstoqueDao {
    Optional<LoteEstoque> findByItemAndId(Long idItem, String numeroLote);
    void salvar(LoteEstoque loteEstoque);
    int atualizar(LoteEstoque loteEstoque);
    List<LoteEstoque> findByItemOrderByValidadeAsc(Long idItem);

}

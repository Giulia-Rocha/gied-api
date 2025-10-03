package com.fiap.giedapi.repository;

import com.fiap.giedapi.domain.model.Movimentacao;

import java.util.List;

public interface MovimentacaoDao {
    void salvar(Movimentacao movimentacao);
    List<Movimentacao> buscarTodas();
}

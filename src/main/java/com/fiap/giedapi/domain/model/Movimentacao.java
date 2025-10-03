package com.fiap.giedapi.domain.model;

import com.fiap.giedapi.domain.enums.TipoMovimentacao;

import java.time.LocalDateTime;

public class Movimentacao {
    private Long id;
    private LocalDateTime dataHora;
    private Integer quantidade;
    private TipoMovimentacao tipoMovimentacao;
    private LoteEstoque lote;

    public Movimentacao(Long id, LocalDateTime dataHora, Integer quantidade, TipoMovimentacao tipoMovimentacao,  LoteEstoque lote) {
        this.id = id;
        this.dataHora = dataHora;
        this.quantidade = quantidade;
        this.tipoMovimentacao = tipoMovimentacao;
        this.lote = lote;
    }

    public Movimentacao() {}

    public LoteEstoque getLote() {
        return lote;
    }

    public void setLote(LoteEstoque lote) {
        this.lote = lote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }
}

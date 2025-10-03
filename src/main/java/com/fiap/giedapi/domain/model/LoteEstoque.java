package com.fiap.giedapi.domain.model;

import java.time.LocalDate;

public class LoteEstoque {
    private Long id;
    private Item item;
    private String lote;
    private LocalDate dataValidade;
    private Integer quantidade;

    public LoteEstoque(Long id, Item item, String lote, LocalDate dataValidade, Integer quantidade) {
        this.id = id;
        this.item = item;
        this.lote = lote;
        this.dataValidade = dataValidade;
        this.quantidade = quantidade;
    }

    public LoteEstoque() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}

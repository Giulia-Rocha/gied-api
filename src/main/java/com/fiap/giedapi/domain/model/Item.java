package com.fiap.giedapi.domain.model;

/**
 * Representa um item genérico no estoque.
 * Classe pai no modelo de herança.
 */
public class Item {

    private Long id;
    private String nome;
    private String descricao;
    private Integer nivelMinEstoque;
    private Integer quantidadeNoEstoque;

    public Item(Long id, String nome, String descricao, Integer nivelMinEstoque, Integer quantidadeNoEstoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.nivelMinEstoque = nivelMinEstoque;
        this.quantidadeNoEstoque = quantidadeNoEstoque;
    }
    public Item(Long id, String nome, String descricao, Integer nivelMinEstoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.nivelMinEstoque = nivelMinEstoque;
    }

    public Item(){}


    public Integer getQuantidadeNoEstoque() {
        return quantidadeNoEstoque;
    }

    public void setQuantidadeNoEstoque(Integer quantidadeNoEstoque) {
        this.quantidadeNoEstoque = quantidadeNoEstoque;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getNivelMinEstoque() {
        return nivelMinEstoque;
    }

    public void setNivelMinEstoque(Integer nivelMinEstoque) {
        this.nivelMinEstoque = nivelMinEstoque;}
    }


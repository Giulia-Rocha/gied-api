package com.fiap.giedapi.repository;

import com.fiap.giedapi.domain.model.Item;

import java.util.List;

public interface ItemDao {
    Long salvar(Item item);
    void atualizar(Item item);
    void delete(Long id);
    Item getById(Long id);
    List<Item> listarTodos();
    List<Item> findByEstoqueBaixo();
}

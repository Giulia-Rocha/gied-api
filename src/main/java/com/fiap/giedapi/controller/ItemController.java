package com.fiap.giedapi.controller;


import com.fiap.giedapi.domain.model.Item;
import com.fiap.giedapi.domain.model.LoteEstoque;
import com.fiap.giedapi.dto.ConsultaEstoqueDTO;
import com.fiap.giedapi.dto.ItemEntradaDTO;
import com.fiap.giedapi.dto.ItemResponseDTO;
import com.fiap.giedapi.dto.ItemSaidaDTO;
import com.fiap.giedapi.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemService service;

    @GetMapping
    public ResponseEntity<List<Item>> listarTodoEstoque(){
        List<Item> items = service.listarEstoque();
        if (items.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().body(items);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ConsultaEstoqueDTO> findById(@PathVariable Long id){
        ConsultaEstoqueDTO response = service.consultarEstoque(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/entrada")
    public ResponseEntity<ItemEntradaDTO> salvar(@RequestBody ItemEntradaDTO dto){

     service.registrarEntrada(dto.id(), dto.quantidade(), dto.numeroLote(), dto.dataValidade());
     return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    @PostMapping("/saida")
    public ResponseEntity<Map<String,String>> registrarSaida(@RequestBody ItemSaidaDTO dto){
        service.registrarSaida(dto.id(), dto.quantidade());

        Map<String, String> response = Map.of(
                "Mensagem", "Saída registrada com sucesso",
                "ID item", dto.id().toString(),
                "Quantidade", dto.quantidade().toString()
        );
        return  ResponseEntity.ok(response);
    }



}

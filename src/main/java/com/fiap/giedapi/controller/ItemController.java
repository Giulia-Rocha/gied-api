package com.fiap.giedapi.controller;


import com.fiap.giedapi.domain.model.Item;
import com.fiap.giedapi.domain.vo.EstoqueInfo;
import com.fiap.giedapi.dto.response.ConsultaEstoqueDTO;
import com.fiap.giedapi.dto.request.ItemEntradaDTO;
import com.fiap.giedapi.dto.request.ItemSaidaDTO;
import com.fiap.giedapi.dto.mappers.ItemMapper;
import com.fiap.giedapi.dto.response.ItemEstoqueBaixoDTO;
import com.fiap.giedapi.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        EstoqueInfo estoqueInfo = service.consultarEstoquePorID(id);
        ConsultaEstoqueDTO response =
                ItemMapper.toConsultaEstoqueDTO(estoqueInfo);
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

    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<ItemEstoqueBaixoDTO>> listarEstoqueBaixo(){
       List<Item> items = service.listarEstoqueBaixo();
       if (items.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
       }
       List<ItemEstoqueBaixoDTO> response = ItemMapper.toEstoqueBaixoDTO(items);
       return ResponseEntity.ok(response);

    }



}

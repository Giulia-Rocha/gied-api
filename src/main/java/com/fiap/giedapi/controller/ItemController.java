package com.fiap.giedapi.controller;


import com.fiap.giedapi.domain.model.Item;
import com.fiap.giedapi.domain.vo.EstoqueInfo;
import com.fiap.giedapi.dto.response.ConsultaEstoqueDTO;
import com.fiap.giedapi.dto.request.ItemEntradaDTO;
import com.fiap.giedapi.dto.request.ItemSaidaDTO;
import com.fiap.giedapi.dto.mappers.ItemMapper;
import com.fiap.giedapi.dto.response.ItemEstoqueBaixoDTO;
import com.fiap.giedapi.exception.EntityNotFoundException;
import com.fiap.giedapi.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

    @Operation(summary = "Listar todo estoque", description = "Lista todos os" +
            " itens do estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna os " +
                    "itens existentes"),
            @ApiResponse(responseCode = "204", description = "Estoque vazio")
    })
    @GetMapping
    public ResponseEntity<List<Item>> listarTodoEstoque(){
        return ResponseEntity.ok().body(service.listarEstoque());
    }

    @Operation(summary = "Buscar um item por id", description = "Busca um " +
            "item por id no estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "retorna as " +
                    "informações do item"),
            @ApiResponse(responseCode = "404", description = "Não existe item" +
                    " com id inexistente")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConsultaEstoqueDTO> findById(@PathVariable Long id){
            EstoqueInfo estoqueInfo = service.consultarEstoquePorID(id);
            ConsultaEstoqueDTO response =
            ItemMapper.toConsultaEstoqueDTO(estoqueInfo);
            return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar Entrada", description = "Registra entrada" +
            " de itens no estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Itens " +
                    "registrados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado no estoque"),
    })
    @PostMapping("/entrada")
    public ResponseEntity<ItemEntradaDTO> salvar(@Valid @RequestBody ItemEntradaDTO dto){

     service.registrarEntrada(dto.id(), dto.quantidade(), dto.numeroLote(), dto.dataValidade());
     return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @Operation(summary = "Registrar saida", description = "Registra saida de " +
            "itens do estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Saída registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Estoque " +
                    "insuficiente"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado no estoque")

    })
    @PostMapping("/saida")
    public ResponseEntity<Map<String,String>> registrarSaida(@Valid @RequestBody ItemSaidaDTO dto){
        service.registrarSaida(dto.id(), dto.quantidade());

        Map<String, String> response = Map.of(
                "Mensagem", "Saída registrada com sucesso",
                "ID item", dto.id().toString(),
                "Quantidade", dto.quantidade().toString()
        );
        return  ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar o estoque baixo", description = "Lista os " +
            "itens que estão com estoque abaixo do limite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista todos os " +
                    "itens encontrados"),
            @ApiResponse(responseCode = "404", description = "Não existe " +
                    "nenhum item com estoque baixo")
    })
    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<ItemEstoqueBaixoDTO>> listarEstoqueBaixo(){
       List<ItemEstoqueBaixoDTO> response = ItemMapper.toEstoqueBaixoDTO(service.listarEstoqueBaixo());
       return ResponseEntity.ok(response);

    }



}

package com.fiap.giedapi.dto.mappers;


import com.fiap.giedapi.domain.model.Item;
import com.fiap.giedapi.domain.vo.EstoqueInfo;
import com.fiap.giedapi.dto.response.ConsultaEstoqueDTO;
import com.fiap.giedapi.dto.response.ItemEstoqueBaixoDTO;


import java.util.ArrayList;
import java.util.List;

public class ItemMapper{

    public static List<ItemEstoqueBaixoDTO> toEstoqueBaixoDTO(List<Item> listaEstoqueBaixo){
        List<ItemEstoqueBaixoDTO> list = new ArrayList<>();
        for (Item item : listaEstoqueBaixo){
            Long id = item.getId();
            String nome = item.getNome();
            int nivelMinimo = item.getNivelMinEstoque();
            int quantidadeNoEstoque = item.getQuantidadeNoEstoque();

            list.add(new ItemEstoqueBaixoDTO(id,nome,nivelMinimo,
                    quantidadeNoEstoque));
        }
        return list;
    }

    public static ConsultaEstoqueDTO toConsultaEstoqueDTO(EstoqueInfo estoqueInfo){
        return new ConsultaEstoqueDTO(estoqueInfo.item(), estoqueInfo.lotes());
    }

}

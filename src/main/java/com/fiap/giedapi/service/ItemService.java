package com.fiap.giedapi.service;

import com.fiap.giedapi.repository.ItemDao;
import com.fiap.giedapi.repository.LoteEstoqueDao;
import com.fiap.giedapi.repository.MovimentacaoDao;
import com.fiap.giedapi.domain.enums.TipoMovimentacao;
import com.fiap.giedapi.domain.model.Item;
import com.fiap.giedapi.domain.model.LoteEstoque;
import com.fiap.giedapi.domain.model.Movimentacao;
import com.fiap.giedapi.dto.ConsultaEstoqueDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class ItemService {
    private final ItemDao itemDao ;
    private final LoteEstoqueDao loteEstoqueDao;
    private final MovimentacaoDao movimentacaoDao;




    public ItemService(ItemDao itemDao, LoteEstoqueDao loteEstoqueDao, MovimentacaoDao movimentacaoDao) {
        this.itemDao = itemDao;
        this.loteEstoqueDao = loteEstoqueDao;
        this.movimentacaoDao = movimentacaoDao;
    }


    public void registrarEntrada(Long idItem, int quantidade, String numeroLote, LocalDate dataValidade){
        //validações de entrada
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        if (numeroLote == null || numeroLote.trim().isEmpty()) {
            throw new IllegalArgumentException("O número do lote é obrigatório.");
        }

        // Regra de Negócio
        Item item = itemDao.getById(idItem);
        if(item==null){
            throw new IllegalStateException("Produto com ID " + idItem + " não encontrado no catálogo. Cadastre o produto primeiro.");
        }

        LoteEstoque loteProcessado = loteEstoqueDao.findByItemAndId(idItem,numeroLote)
                .map(loteExistente -> {
            if (!loteExistente.getDataValidade().equals(dataValidade)) {
                throw new IllegalStateException("Inconsistência de dados: a data de validade do lote não corresponde à já cadastrada.");
            }
            loteExistente.setQuantidade(loteExistente.getQuantidade() + quantidade);
            loteEstoqueDao.atualizar(loteExistente);
            return loteExistente;
        })
                .orElseGet(() -> {
                    LoteEstoque novoLote = new LoteEstoque(null, item, numeroLote, dataValidade, quantidade);
                    loteEstoqueDao.salvar(novoLote);
                    return novoLote;
                });
        Movimentacao mov = new Movimentacao(null, LocalDateTime.now(), quantidade, TipoMovimentacao.ENTRADA, loteProcessado);
        movimentacaoDao.salvar(mov);

    }
//    public void RegistrarSaida(Long idItem, int quantidade){
//        if (quantidade <= 0) {
//            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
//        }
//
//        List<LoteEstoque> lotesOrdenados = loteEstoqueDao.findByItemOrderByValidadeAsc(idItem);
//
//        int estoqueTotal = lotesOrdenados.stream()
//                .mapToInt(LoteEstoque::getQuantidade)
//                .sum();
//        if(estoqueTotal <= quantidade){
//            throw new IllegalStateException("Estoque insuficiente. Total disponivel " + estoqueTotal + ".");
//        }
//        int quantidadeFinal = quantidade;
//
//        for (LoteEstoque lote : lotesOrdenados) {
//            if(lote.getQuantidade() >= quantidadeFinal){
//                lote.setQuantidade(lote.getQuantidade() - quantidadeFinal);
//                quantidadeFinal = 0;
//                loteEstoqueDao.atualizar(lote);
//                break;
//            }else{
//                quantidadeFinal -= lote.getQuantidade();
//                lote.setQuantidade(0);
//                loteEstoqueDao.atualizar(lote);
//            }
//        }
//        if(quantidadeFinal > 0){
//            throw new RuntimeException("Erro inesperado no cálculo de baixa de estoque. Estoque insuficiente.");
//        }
//        Movimentacao mov = new Movimentacao(null, LocalDateTime.now(), quantidade, TipoMovimentacao.RETIRADA, lote);
//        movimentacaoDao.salvar(mov);
//    }
    public List<LoteEstoque> ConsultarEstoque(Long idItem){
        //1. validação do id
        Item item = itemDao.getById(idItem);
        if(item==null){
            throw new IllegalStateException("Item com Id "+idItem+" não encontrado.");
        }
        //2.pede ao dao para buscar a lista de items com esse id
        List<LoteEstoque> lotes = loteEstoqueDao.findByItemOrderByValidadeAsc(idItem);
        return new ConsultaEstoqueDTO(item, lotes).lotes();

    }
    public List<Item> listarEstoque(){
       return itemDao.listarTodos();
    }
    public List<Item> listarEstoqueBaixo(){
        return itemDao.findByEstoqueBaixo();
    }
}

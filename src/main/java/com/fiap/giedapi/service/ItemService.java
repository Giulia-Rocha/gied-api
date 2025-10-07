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
import java.util.ArrayList;
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
    public void registrarSaida(Long idItem, int quantidade) {
        // 1. Validações de entrada
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        // 2. Verificar se o item existe
        Item item = itemDao.getById(idItem);
        if (item == null) {
            throw new IllegalStateException("Produto com ID " + idItem + " não encontrado no catálogo.");
        }

        // 3. Buscar lotes ordenados por validade (FIFO)
        List<LoteEstoque> lotesOrdenados = loteEstoqueDao.findByItemOrderByValidadeAsc(idItem);

        if (lotesOrdenados.isEmpty()) {
            throw new IllegalStateException("Não há estoque disponível para este item.");
        }

        // 4. Calcular estoque total
        int estoqueTotal = lotesOrdenados.stream()
                .mapToInt(LoteEstoque::getQuantidade)
                .sum();

        if (estoqueTotal < quantidade) {
            throw new IllegalStateException(
                    "Estoque insuficiente. Disponível: " + estoqueTotal +
                            ", Solicitado: " + quantidade
            );
        }

        // 5. Processar baixa nos lotes (FIFO) e rastrear lotes afetados
        List<LoteEstoque> lotesProcessados = new ArrayList<>();
        int quantidadeRestante = quantidade;

        for (LoteEstoque lote : lotesOrdenados) {
            if (quantidadeRestante <= 0) {
                break; // Já processou toda a quantidade necessária
            }

            int quantidadeAnterior = lote.getQuantidade();

            if (lote.getQuantidade() >= quantidadeRestante) {
                // Este lote tem quantidade suficiente para completar a saída
                lote.setQuantidade(lote.getQuantidade() - quantidadeRestante);
                quantidadeRestante = 0;
            } else {
                // Este lote não tem quantidade suficiente, zera e continua
                quantidadeRestante -= lote.getQuantidade();
                lote.setQuantidade(0);
            }

            // Atualiza o lote no banco
            loteEstoqueDao.atualizar(lote);

            // Adiciona à lista de lotes processados
            lotesProcessados.add(lote);
        }

        // 6. Verificação de segurança (não deveria acontecer)
        if (quantidadeRestante > 0) {
            throw new RuntimeException(
                    "Erro inesperado no cálculo de baixa de estoque. " +
                            "Quantidade não processada: " + quantidadeRestante
            );
        }

        // 7. Registrar movimentação usando o primeiro lote afetado
        // (ou você pode criar um relacionamento N:N entre Movimentacao e Lote)
        LoteEstoque lotePrincipal = lotesProcessados.get(0);

        Movimentacao mov = new Movimentacao(
                null,
                LocalDateTime.now(),
                quantidade,
                TipoMovimentacao.RETIRADA,
                lotePrincipal
        );
        movimentacaoDao.salvar(mov);
    }
    public ConsultaEstoqueDTO consultarEstoque(Long idItem){
        //1. validação do id
        Item item = itemDao.getById(idItem);
        if(item==null){
            throw new IllegalStateException("Item com Id "+idItem+" não encontrado.");
        }
        //2.pede ao dao para buscar a lista de items com esse id
        List<LoteEstoque> lotes = loteEstoqueDao.findByItemOrderByValidadeAsc(idItem);
        return new ConsultaEstoqueDTO(item, lotes);

    }
    public List<Item> listarEstoque(){
       return itemDao.listarTodos();
    }
    public List<Item> listarEstoqueBaixo(){
        return itemDao.findByEstoqueBaixo();
    }
}

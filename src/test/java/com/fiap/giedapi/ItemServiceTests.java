package com.fiap.giedapi;

import com.fiap.giedapi.domain.model.Item;
import com.fiap.giedapi.domain.model.LoteEstoque;
import com.fiap.giedapi.domain.vo.EstoqueInfo;
import com.fiap.giedapi.repository.ItemDao;
import com.fiap.giedapi.repository.LoteEstoqueDao;
import com.fiap.giedapi.repository.MovimentacaoDao;
import com.fiap.giedapi.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTests {

        @Mock // Cria um mock para o ItemDao
        private ItemDao itemDao;

        @Mock // Cria um mock para o LoteEstoqueDao
        private LoteEstoqueDao loteEstoqueDao;

        @Mock
        private MovimentacaoDao movimentacaoDao;

        @InjectMocks // Cria a instância de ItemService e injeta os mocks acima
        private ItemService itemService;

        private Item item;
        private Long idItemExistente = 1L;
        private Long idItemInexistente = 99L;

        @BeforeEach
        void setUp() {
            // Objeto base para os testes
            item = new Item(idItemExistente, "Seringa", "Seringa Descartável 5ml", 20);
        }

        @Test
        void deveRegistrarEntradaParaNovoLoteComSucesso() {
            // Cenário
            when(itemDao.getById(idItemExistente)).thenReturn(item);
            when(loteEstoqueDao.findByItemAndId(idItemExistente, "LOTE01")).thenReturn(Optional.empty());

            // Ação
            itemService.registrarEntrada(idItemExistente, 100, "LOTE01", LocalDate.now().plusYears(1));

            // Verificação
            verify(itemDao).getById(idItemExistente); // Verifica se getById foi chamado
            verify(loteEstoqueDao).findByItemAndId(idItemExistente, "LOTE01");
            verify(loteEstoqueDao).salvar(any(LoteEstoque.class)); // Verifica se o método salvar foi chamado
            verify(loteEstoqueDao, never()).atualizar(any(LoteEstoque.class)); // Garante que o método atualizar NÃO foi chamado
        }

        @Test
        void deveRegistrarEntradaParaLoteExistenteAtualizandoQuantidade() {
            // Cenário
            LoteEstoque loteExistente = new LoteEstoque(10L, item, "LOTE02", LocalDate.now().plusYears(1), 50);

            when(itemDao.getById(idItemExistente)).thenReturn(item);
            when(loteEstoqueDao.findByItemAndId(idItemExistente, "LOTE02")).thenReturn(Optional.of(loteExistente));

            // Ação
            itemService.registrarEntrada(idItemExistente, 30, "LOTE02", LocalDate.now().plusYears(1));

            // Verificação
            assertEquals(80, loteExistente.getQuantidade()); // Verifica se a quantidade foi somada corretamente
            verify(loteEstoqueDao).atualizar(loteExistente); // Verifica se o método atualizar foi chamado
            verify(loteEstoqueDao, never()).salvar(any(LoteEstoque.class)); // Garante que o salvar NÃO foi chamado
        }

        @Test
        void deveLancarExcecaoAoRegistrarEntradaDeItemInexistente() {
            // Cenário
            when(itemDao.getById(idItemInexistente)).thenReturn(null);

            // Ação e Verificação
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                itemService.registrarEntrada(idItemInexistente, 50, "LOTE03", LocalDate.now());
            });

            assertEquals("Produto com ID " + idItemInexistente + " não encontrado no catálogo. Cadastre o produto primeiro.", exception.getMessage());
        }

        @Test
        void deveRegistrarSaidaComEstoqueSuficienteEmUmLote() {
            // Cenário
            LoteEstoque lote1 = new LoteEstoque(1L, item, "LOTE-A", LocalDate.now().plusMonths(6), 100);
            List<LoteEstoque> lotes = List.of(lote1);

            when(itemDao.getById(idItemExistente)).thenReturn(item);
            when(loteEstoqueDao.findByItemOrderByValidadeAsc(idItemExistente)).thenReturn(lotes);

            // Ação
            itemService.registrarSaida(idItemExistente, 40);

            // Verificação
            assertEquals(60, lote1.getQuantidade()); // Verifica a baixa no lote
            verify(loteEstoqueDao).atualizar(lote1);
        }

        @Test
        void deveRegistrarSaidaComEstoqueSuficienteEmMultiplosLotes() {
            // Cenário: Saída de 80, consumindo um lote inteiro e parte de outro
            LoteEstoque lote1 = new LoteEstoque(1L, item, "LOTE-A", LocalDate.now().plusMonths(6), 50);
            LoteEstoque lote2 = new LoteEstoque(2L, item, "LOTE-B", LocalDate.now().plusMonths(12), 100);
            List<LoteEstoque> lotes = new ArrayList<>(List.of(lote1, lote2)); // Usa ArrayList para permitir modificação
            when(itemDao.getById(idItemExistente)).thenReturn(item);
            when(loteEstoqueDao.findByItemOrderByValidadeAsc(idItemExistente)).thenReturn(lotes);

            // Ação
            itemService.registrarSaida(idItemExistente, 80);

            // Verificação
            assertEquals(0, lote1.getQuantidade()); // Primeiro lote foi totalmente consumido
            assertEquals(70, lote2.getQuantidade()); // 30 foram retirados do segundo lote (50 do lote1 + 30 do lote2 = 80)
            verify(loteEstoqueDao).atualizar(lote1);
            verify(loteEstoqueDao).atualizar(lote2);
        }

        @Test
        void deveLancarExcecaoAoRegistrarSaidaComEstoqueInsuficiente() {
            // Cenário
            LoteEstoque lote1 = new LoteEstoque(1L, item, "LOTE-A", LocalDate.now().plusMonths(6), 30);
            List<LoteEstoque> lotes = List.of(lote1);

            when(itemDao.getById(idItemExistente)).thenReturn(item);
            when(loteEstoqueDao.findByItemOrderByValidadeAsc(idItemExistente)).thenReturn(lotes);

            // Ação e Verificação
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                itemService.registrarSaida(idItemExistente, 50);
            });

            assertEquals("Estoque insuficiente. Disponível: 30, Solicitado: 50", exception.getMessage());

        }

        @Test
        void deveConsultarEstoqueDeItemExistente() {
            // Cenário
            when(itemDao.getById(idItemExistente)).thenReturn(item);
            when(loteEstoqueDao.findByItemOrderByValidadeAsc(idItemExistente)).thenReturn(List.of(new LoteEstoque()));

            // Ação
            EstoqueInfo item = itemService.consultarEstoquePorID(idItemExistente);

            // Verificação
            assertNotNull(item);
            verify(itemDao).getById(idItemExistente);
            verify(loteEstoqueDao).findByItemOrderByValidadeAsc(idItemExistente);
        }

        @Test
        void deveLancarExcecaoAoConsultarEstoqueDeItemInexistente() {
            // Cenário
            when(itemDao.getById(idItemInexistente)).thenReturn(null);

            // Ação e Verificação
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                itemService.consultarEstoquePorID(idItemInexistente);
            });

            assertEquals("Item com Id " + idItemInexistente + " não encontrado.", exception.getMessage());
        }
    }


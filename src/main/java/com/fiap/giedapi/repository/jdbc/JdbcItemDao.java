package com.fiap.giedapi.repository.jdbc;

import com.fiap.giedapi.config.OracleConnectionFactory;
import com.fiap.giedapi.repository.ItemDao;
import com.fiap.giedapi.domain.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcItemDao implements ItemDao {
    @Override
    public Long salvar(Item item) {
        String sql = "INSERT INTO item (NM_ITEM, DS_ITEM, NR_NIVEL_MIN_ESTOQUE) VALUES (?, ?, ?)";
        try (Connection con = OracleConnectionFactory.getConnection();
             PreparedStatement st = con.prepareStatement(sql, new String[]{"id"})) {

            st.setString(1, item.getNome());
            st.setString(2, item.getDescricao());
            st.setInt(3, item.getNivelMinEstoque());

            st.executeUpdate();

            // Recupera o ID gerado pelo banco de dados.
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            return 0L; // Retorna 0 se não conseguiu recuperar o ID.

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o item no banco de dados.", e);
        }
    }

    @Override
    public void atualizar(Item item) {
        String sql = "UPDATE item SET NM_ITEM = ?, DS_ITEM = ?, NR_NIVEL_MIN_ESTOQUE = ? WHERE ID_ITEM = ?";
        try (Connection con = OracleConnectionFactory.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, item.getNome());
            st.setString(2, item.getDescricao());
            st.setInt(3, item.getNivelMinEstoque());
            st.setLong(4, item.getId());

            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o item no banco de dados.", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM ITEM WHERE ID_ITEM = ?";
        try (Connection con = OracleConnectionFactory.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setLong(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar o item no banco de dados.", e);
        }
    }

    @Override
    public Item getById(Long id) {
        String sql = "SELECT * FROM ITEM WHERE ID_ITEM = ?";
        try (Connection con = OracleConnectionFactory.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setLong(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getLong("ID_ITEM"));
                    item.setNome(rs.getString("NM_ITEM"));
                    item.setDescricao(rs.getString("DS_ITEM"));
                    item.setNivelMinEstoque(rs.getInt("NR_NIVEL_MIN_ESTOQUE"));
                    return item;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o item por ID.", e);
        }
        return null; // Retorna null se nenhum item for encontrado.
    }

    @Override
    public List<Item> listarTodos() {
        String sql = "SELECT * FROM ITEM";
        List<Item> todosOsItens = new ArrayList<>();
        try (Connection con = OracleConnectionFactory.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                todosOsItens.add(mapRowToItem(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todos os itens.", e);

        }
        return todosOsItens;
    }

    @Override
    public List<Item> findByEstoqueBaixo() {
        String sql = """
            SELECT i.ID_ITEM, i.NM_ITEM, i.DS_ITEM, i.NR_NIVEL_MIN_ESTOQUE, SUM(l.NR_QUANTIDADE) AS TOTAL_QUANTIDADE
                        FROM ITEM i
                        JOIN LOTE l ON i.ID_ITEM = l.ID_ITEM
                        GROUP BY i.ID_ITEM, i.NM_ITEM, i.ID_ITEM, i.DS_ITEM, i.NR_NIVEL_MIN_ESTOQUE
                        HAVING SUM(l.NR_QUANTIDADE) <= i.NR_NIVEL_MIN_ESTOQUE
        """;
        List<Item> itensComEstoqueBaixo = new ArrayList<>();
        try (Connection con = OracleConnectionFactory.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                itensComEstoqueBaixo.add(mapRowToItem(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens com estoque baixo.", e);
        }
        return itensComEstoqueBaixo;
    }

    private Item mapRowToItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getLong("ID_ITEM"));
        item.setNome(rs.getString("NM_ITEM"));
        item.setDescricao(rs.getString("DS_ITEM"));
        item.setNivelMinEstoque(rs.getInt("NR_NIVEL_MIN_ESTOQUE"));

        // Lê a quantidade apenas se a coluna existir
        try {
            item.setQuantidadeNoEstoque(rs.getInt("TOTAL_QUANTIDADE"));
        } catch (SQLException e) {
            // coluna não existe na query, ignora
        }

        return item;
    }
}

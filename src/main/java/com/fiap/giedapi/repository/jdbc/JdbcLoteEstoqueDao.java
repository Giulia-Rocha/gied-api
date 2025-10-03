package com.fiap.giedapi.repository.jdbc;

import com.fiap.giedapi.config.OracleConnectionFactory;
import com.fiap.giedapi.repository.LoteEstoqueDao;
import com.fiap.giedapi.domain.model.LoteEstoque;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository
public class JdbcLoteEstoqueDao implements LoteEstoqueDao {

    @Override
    public Optional<LoteEstoque> findByItemAndId(Long idItem, String numeroLote) {
        String sql = "SELECT * FROM LOTE WHERE ID_ITEM = ? AND NR_LOTE = ?";

        try(Connection con = OracleConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql)){

            st.setLong(1,idItem);
            st.setString(2,numeroLote);

            try(ResultSet rs = st.executeQuery()){
                if(rs.next()){
                    return Optional.of(mapRowToLoteEstoque(rs));
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("Erro ao buscar lote por item e número");
        }
        return Optional.empty();
    }

    @Override
    public void salvar(LoteEstoque loteEstoque) {
        String sqlId = "SELECT SEQ_LOTE.NEXTVAL FROM DUAL";
        String sql = "INSERT INTO LOTE (ID_LOTE,ID_ITEM, NR_LOTE, DT_VALIDADE, NR_QUANTIDADE) VALUES ( ?,?, ?, ? , ?)";
        try (Connection con = OracleConnectionFactory.getConnection();
             PreparedStatement stId = con.prepareStatement(sqlId);
             ResultSet rsId = stId.executeQuery()) {

            if (!rsId.next()) {
                throw new SQLException("Não foi possível gerar um ID para o lote.");
            }
            long idGerado = rsId.getLong(1);
            loteEstoque.setId(idGerado); // Define o ID no objeto

            try (PreparedStatement stInsert = con.prepareStatement(sql)) {
                stInsert.setLong(1, idGerado); // 1. Usa o ID gerado
                stInsert.setLong(2, loteEstoque.getItem().getId()); // 2. ID do item
                stInsert.setString(3, loteEstoque.getLote()); // 3. Número do lote
                stInsert.setDate(4, java.sql.Date.valueOf(loteEstoque.getDataValidade())); // 4. Validade
                stInsert.setInt(5, loteEstoque.getQuantidade()); // 5. Quantidade
                stInsert.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar no BD: " + e.getMessage(), e);
        }
    }

    @Override
    public int atualizar(LoteEstoque loteEstoque) {
        String sql = "UPDATE LOTE SET NR_QUANTIDADE = ? WHERE ID_LOTE= ?";
        try(Connection con = OracleConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql)){
            st.setInt(1, loteEstoque.getQuantidade());
            st.setLong(2, loteEstoque.getId());

            return  st.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException("Erro ao atualizar no BD "+e.getMessage());
        }
    }

    @Override
    public List<LoteEstoque> findByItemOrderByValidadeAsc(Long idItem) {
        String sql = "SELECT * FROM LOTE WHERE ID_ITEM = ? ORDER BY DT_VALIDADE ASC";
        List<LoteEstoque> lotesOrdenados = new ArrayList<>();
        try(Connection con = OracleConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql)){
            st.setLong(1, idItem);
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                // Adiciona o lote totalmente montado à lista de retorno.
                lotesOrdenados.add(mapRowToLoteEstoque(rs));
            }


        }catch(SQLException e){
            throw new RuntimeException("Erro ao buscar lotes "+e.getMessage());
        }
        return lotesOrdenados;
    }

    private LoteEstoque mapRowToLoteEstoque(ResultSet rs) throws SQLException {
        LoteEstoque loteEstoque = new LoteEstoque();
        loteEstoque.setId(rs.getLong("ID_LOTE"));
        loteEstoque.setQuantidade(rs.getInt("NR_QUANTIDADE"));
        loteEstoque.setLote(rs.getString("NR_LOTE"));
        loteEstoque.setDataValidade(rs.getDate("DT_VALIDADE").toLocalDate());
        return loteEstoque;
    }
}

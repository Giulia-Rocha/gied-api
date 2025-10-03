package com.fiap.giedapi.repository.jdbc;

import com.fiap.giedapi.config.OracleConnectionFactory;
import com.fiap.giedapi.repository.MovimentacaoDao;
import com.fiap.giedapi.domain.enums.TipoMovimentacao;
import com.fiap.giedapi.domain.model.Movimentacao;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class JdbcMovimentacaoDao implements MovimentacaoDao {
    @Override
    public void salvar(Movimentacao movimentacao) {
        String sqlId = "SELECT SEQ_MOV.NEXTVAL FROM DUAL";
        String sqlInsert = "INSERT INTO MOVIMENTACAO (ID_MOVIMENTACAO, DT_HR_MOVIMENTACAO, NR_QUANTIDADE_MOV, ID_TP_MOVIMENTACAO, ID_LOTE) VALUES (?, ?, ?, ?,?)";

        try (Connection con = OracleConnectionFactory.getConnection();
             PreparedStatement stId = con.prepareStatement(sqlId);
             ResultSet rsId = stId.executeQuery()) {

            if (!rsId.next()) {
                throw new SQLException("Não foi possível gerar um ID para a movimentação.");
            }
            long idGerado = rsId.getLong(1);
            movimentacao.setId(idGerado);

            try (PreparedStatement stInsert = con.prepareStatement(sqlInsert)) {
                stInsert.setLong(1, idGerado);
                stInsert.setTimestamp(2, Timestamp.valueOf(movimentacao.getDataHora()));
                stInsert.setInt(3, movimentacao.getQuantidade());
                int tipoId = movimentacao.getTipoMovimentacao() == TipoMovimentacao.ENTRADA ? 1 : 2;
                stInsert.setInt(4, tipoId);
                stInsert.setLong(5, movimentacao.getLote().getId());
                stInsert.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar movimentação no BD: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Movimentacao> buscarTodas() {
        List<Movimentacao> movimentacoes = new ArrayList<>();
        // Query para buscar todas as movimentações, ordenadas da mais recente para a mais antiga
        String sql = "SELECT * FROM MOVIMENTACAO ORDER BY DT_HR_MOVIMENTACAO DESC";

        try (Connection con = OracleConnectionFactory.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Movimentacao mov = new Movimentacao();
                mov.setId(rs.getLong("ID_MOVIMENTACAO"));
                mov.setDataHora(rs.getTimestamp("DT_HR_MOVIMENTACAO").toLocalDateTime());
                mov.setQuantidade(rs.getInt("NR_QUANTIDADE_MOV"));
                int tipoId = rs.getInt("ID_TP_MOVIMENTACAO");
                mov.setTipoMovimentacao(tipoId == 1 ? TipoMovimentacao.ENTRADA : TipoMovimentacao.RETIRADA);
                movimentacoes.add(mov);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar histórico de movimentações: " + e.getMessage(), e);
        }
        return movimentacoes;
    }

}

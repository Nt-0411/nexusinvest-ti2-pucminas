package DAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.ItemSimulacao;

public class ItemSimulacaoDao extends DAO {

    public ItemSimulacaoDao() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

   
    public List<ItemSimulacao> getBySimulacao(int id_simulacao) {
        List<ItemSimulacao> lista = new ArrayList<>();
        try {
            PreparedStatement st = conexao.prepareStatement(
                "SELECT i.*, a.ticket, a.nome_ativo, a.tipo " +
                "FROM item_simulacao i " +
                "JOIN ativo_financeiro a ON i.id_ativo = a.id_ativo " +
                "WHERE i.id_simulacao = ? " +
                "ORDER BY i.id_item",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            st.setInt(1, id_simulacao);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
            st.close();
        } catch (Exception e) {
            System.err.println("ItemSimulacaoDao.getBySimulacao: " + e.getMessage());
        }
        return lista;
    }

    public ItemSimulacao get(int id_item) {
        ItemSimulacao item = null;
        try {
            PreparedStatement st = conexao.prepareStatement(
                "SELECT i.*, a.ticket, a.nome_ativo, a.tipo " +
                "FROM item_simulacao i " +
                "JOIN ativo_financeiro a ON i.id_ativo = a.id_ativo " +
                "WHERE i.id_item = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            st.setInt(1, id_item);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                item = mapRow(rs);
            }
            st.close();
        } catch (Exception e) {
            System.err.println("ItemSimulacaoDao.get: " + e.getMessage());
        }
        return item;
    }

    public int insert(ItemSimulacao item) {
        int generatedId = -1;
        try {
            PreparedStatement st = conexao.prepareStatement(
                "INSERT INTO item_simulacao (quantidade, data_compra, preco_compra, id_simulacao, id_ativo) " +
                "VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, item.getQuantidade());
            st.setDate(2, Date.valueOf(item.getData_compra()));
            st.setBigDecimal(3, item.getPreco_compra());
            st.setInt(4, item.getId_simulacao());
            st.setInt(5, item.getId_ativo());
            st.executeUpdate();
            ResultSet keys = st.getGeneratedKeys();
            if (keys.next()) {
                generatedId = keys.getInt(1);
            }
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return generatedId;
    }

    public boolean delete(int id_item) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM item_simulacao WHERE id_item = " + id_item);
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    private ItemSimulacao mapRow(ResultSet rs) throws SQLException {
        ItemSimulacao i = new ItemSimulacao();
        i.setId_item(rs.getInt("id_item"));
        i.setQuantidade(rs.getInt("quantidade"));
        Date d = rs.getDate("data_compra");
        if (d != null) i.setData_compra(d.toLocalDate());
        i.setPreco_compra(rs.getBigDecimal("preco_compra"));
        i.setId_simulacao(rs.getInt("id_simulacao"));
        i.setId_ativo(rs.getInt("id_ativo"));
        // Campos do join
        try { i.setTicket(rs.getString("ticket")); }         catch (SQLException ignored) {}
        try { i.setNome_ativo(rs.getString("nome_ativo")); } catch (SQLException ignored) {}
        try { i.setTipo(rs.getString("tipo")); }             catch (SQLException ignored) {}
        return i;
    }
}

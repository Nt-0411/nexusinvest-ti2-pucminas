package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.AtivoFinanceiro;

public class AtivoFinanceiroDao extends DAO {

    public AtivoFinanceiroDao() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

   
    public AtivoFinanceiro findByTicket(String ticket) {
        AtivoFinanceiro ativo = null;
        try {
            PreparedStatement st = conexao.prepareStatement(
                "SELECT * FROM ativo_financeiro WHERE UPPER(ticket) = UPPER(?)",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            st.setString(1, ticket);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                ativo = mapRow(rs);
            }
            st.close();
        } catch (Exception e) {
            System.err.println("AtivoFinanceiroDao.findByTicket: " + e.getMessage());
        }
        return ativo;
    }

    public AtivoFinanceiro get(int id_ativo) {
        AtivoFinanceiro ativo = null;
        try {
            Statement st = conexao.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(
                "SELECT * FROM ativo_financeiro WHERE id_ativo = " + id_ativo);
            if (rs.next()) {
                ativo = mapRow(rs);
            }
            st.close();
        } catch (Exception e) {
            System.err.println("AtivoFinanceiroDao.get: " + e.getMessage());
        }
        return ativo;
    }

  
    public int insert(AtivoFinanceiro ativo) {
        int generatedId = -1;
        try {
            PreparedStatement st = conexao.prepareStatement(
                "INSERT INTO ativo_financeiro (ticket, nome_ativo, setor, tipo) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            st.setString(1, ativo.getTicket().toUpperCase());
            st.setString(2, ativo.getNome_ativo());
            st.setString(3, ativo.getSetor());
            st.setString(4, ativo.getTipo());
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

   
    public int getOrCreateId(String ticket, String nome_ativo, String setor, String tipo) {
        AtivoFinanceiro existente = findByTicket(ticket);
        if (existente != null) {
            return existente.getId_ativo();
        }
        AtivoFinanceiro novo = new AtivoFinanceiro(-1, ticket, nome_ativo, setor, tipo);
        return insert(novo);
    }

    private AtivoFinanceiro mapRow(ResultSet rs) throws SQLException {
        AtivoFinanceiro a = new AtivoFinanceiro();
        a.setId_ativo(rs.getInt("id_ativo"));
        a.setTicket(rs.getString("ticket"));
        a.setNome_ativo(rs.getString("nome_ativo"));
        a.setSetor(rs.getString("setor"));
        a.setTipo(rs.getString("tipo"));
        return a;
    }
}

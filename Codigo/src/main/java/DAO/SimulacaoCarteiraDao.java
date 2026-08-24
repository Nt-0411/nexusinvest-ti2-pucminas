package DAO;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import model.SimulacaoCarteira;

public class SimulacaoCarteiraDao extends DAO {

    public SimulacaoCarteiraDao() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public SimulacaoCarteira getByUsuario(int id_usuario) {
        SimulacaoCarteira carteira = null;
        try {
            PreparedStatement st = conexao.prepareStatement(
                "SELECT * FROM simulacao_carteira WHERE id_usuario = ? ORDER BY id_simulacao DESC LIMIT 1",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            st.setInt(1, id_usuario);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                carteira = mapRow(rs);
            }
            st.close();
        } catch (Exception e) {
            System.err.println("SimulacaoCarteiraDao.getByUsuario: " + e.getMessage());
        }
        return carteira;
    }

    public int insert(SimulacaoCarteira carteira) {
        int generatedId = -1;
        try {
            PreparedStatement st = conexao.prepareStatement(
                "INSERT INTO simulacao_carteira (saldo, id_usuario) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            st.setBigDecimal(1, carteira.getSaldo());
            st.setInt(2, carteira.getId_usuario());
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

    public boolean updateSaldo(int id_simulacao, BigDecimal novoSaldo) {
        boolean status = false;
        try {
            PreparedStatement st = conexao.prepareStatement(
                "UPDATE simulacao_carteira SET saldo = ? WHERE id_simulacao = ?");
            st.setBigDecimal(1, novoSaldo);
            st.setInt(2, id_simulacao);
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    
    public SimulacaoCarteira getOrCreate(int id_usuario) {
        SimulacaoCarteira carteira = getByUsuario(id_usuario);
        if (carteira == null) {
            SimulacaoCarteira nova = new SimulacaoCarteira();
            nova.setId_usuario(id_usuario);
            nova.setSaldo(BigDecimal.ZERO);
            int id = insert(nova);
            nova.setId_simulacao(id);
            carteira = nova;
        }
        return carteira;
    }

    private SimulacaoCarteira mapRow(ResultSet rs) throws SQLException {
        SimulacaoCarteira c = new SimulacaoCarteira();
        c.setId_simulacao(rs.getInt("id_simulacao"));
        c.setSaldo(rs.getBigDecimal("saldo"));
        Timestamp ts = rs.getTimestamp("data_simulacao");
        if (ts != null) c.setData_simulacao(ts.toLocalDateTime());
        c.setId_usuario(rs.getInt("id_usuario"));
        return c;
    }
}

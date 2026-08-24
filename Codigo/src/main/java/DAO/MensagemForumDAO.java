package DAO;

import model.MensagemForum;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MensagemForumDAO extends DAO {

    public MensagemForumDAO() {
        super();
        conectar();
    }

    public boolean insert(MensagemForum msg) {
        boolean status = false;
        try {
            String sql = "INSERT INTO mensagem_forum (id_topico, id_usuario, conteudo, data_publicacao) VALUES (?, ?, ?, ?)";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, msg.getIdTopico());
            st.setInt(2, msg.getIdUsuario());
            st.setString(3, msg.getConteudo());
            st.setTimestamp(4, Timestamp.valueOf(msg.getDataPublicacao()));
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    // Busca todas as mensagens de um tópico específico
    public List<MensagemForum> getByTopico(int idTopico) {
        List<MensagemForum> mensagens = new ArrayList<>();
        try {
            String sql = "SELECT m.*, u.nome, u.foto FROM mensagem_forum m " +
                         "INNER JOIN usuario u ON m.id_usuario = u.id_usuario " +
                         "WHERE m.id_topico = " + idTopico + " ORDER BY m.data_publicacao ASC";
            
            Statement st = conexao.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                MensagemForum msg = new MensagemForum(
                    rs.getInt("id_mensagem"),
                    rs.getInt("id_topico"),
                    rs.getInt("id_usuario"),
                    rs.getString("conteudo"),
                    rs.getTimestamp("data_publicacao").toLocalDateTime()
                );
                msg.setNomeUsuario(rs.getString("nome"));
                msg.setFotoUsuario(rs.getString("foto"));
                mensagens.add(msg);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return mensagens;
    }

    // Busca uma única mensagem pelo ID
    public MensagemForum get(int idMensagem) {
        MensagemForum msg = null;
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM mensagem_forum WHERE id_mensagem = " + idMensagem;
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                msg = new MensagemForum(
                    rs.getInt("id_mensagem"),
                    rs.getInt("id_topico"),
                    rs.getInt("id_usuario"),
                    rs.getString("conteudo"),
                    rs.getTimestamp("data_publicacao").toLocalDateTime()
                );
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return msg;
    }

    // Atualiza o conteúdo da mensagem
    public boolean update(MensagemForum msg) {
        boolean status = false;
        try {
            String sql = "UPDATE mensagem_forum SET conteudo = ? WHERE id_mensagem = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, msg.getConteudo());
            st.setInt(2, msg.getIdMensagem());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    // Exclui a mensagem
    public boolean delete(int idMensagem) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM mensagem_forum WHERE id_mensagem = " + idMensagem);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
}
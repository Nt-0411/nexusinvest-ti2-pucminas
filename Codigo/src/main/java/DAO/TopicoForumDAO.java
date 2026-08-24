package DAO;

import model.TopicoForum;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TopicoForumDAO extends DAO {    
    
    public TopicoForumDAO() {
        super();
        conectar();
    }
    
    public void finalize() {
        close();
    }
    
    public boolean insert(TopicoForum topico) {
        boolean status = false;
        try {
            String sql = "INSERT INTO topico_forum (id_usuario, titulo, conteudo, data_publicacao, status) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, topico.getIdUsuario());
            st.setString(2, topico.getTitulo());
            st.setString(3, topico.getConteudo());
            st.setTimestamp(4, Timestamp.valueOf(topico.getDataPublicacao()));
            st.setString(5, topico.getStatus());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }

    public TopicoForum get(int idTopico) {
        TopicoForum topico = null;
        try {
            String sql = "SELECT t.*, u.nome, u.foto FROM topico_forum t " +
                         "INNER JOIN usuario u ON t.id_usuario = u.id_usuario " +
                         "WHERE t.id_topico = " + idTopico;
            
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                topico = new TopicoForum(
                    rs.getInt("id_topico"),
                    rs.getInt("id_usuario"),
                    rs.getString("titulo"),
                    rs.getString("conteudo"),
                    rs.getTimestamp("data_publicacao").toLocalDateTime(),
                    rs.getString("status")
                );
                topico.setNomeUsuario(rs.getString("nome"));
                topico.setFotoUsuario(rs.getString("foto"));
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return topico;
    }

    public List<TopicoForum> get() {
        return getList("SELECT t.*, u.nome, u.foto FROM topico_forum t INNER JOIN usuario u ON t.id_usuario = u.id_usuario ORDER BY t.id_topico ASC");
    }
    
    public List<TopicoForum> getOrderByID() {
        return getList("SELECT t.*, u.nome, u.foto FROM topico_forum t INNER JOIN usuario u ON t.id_usuario = u.id_usuario ORDER BY t.id_topico ASC");
    }
    
    public List<TopicoForum> getOrderByTitulo() {
        return getList("SELECT t.*, u.nome, u.foto FROM topico_forum t INNER JOIN usuario u ON t.id_usuario = u.id_usuario ORDER BY t.titulo ASC");
    }
    
    public List<TopicoForum> getOrderByDataPublicacao() {
        return getList("SELECT t.*, u.nome, u.foto FROM topico_forum t INNER JOIN usuario u ON t.id_usuario = u.id_usuario ORDER BY t.data_publicacao DESC");
    }
    
    private List<TopicoForum> getList(String sql) {
        List<TopicoForum> topicos = new ArrayList<TopicoForum>();
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                TopicoForum t = new TopicoForum(
                    rs.getInt("id_topico"),
                    rs.getInt("id_usuario"),
                    rs.getString("titulo"),
                    rs.getString("conteudo"),
                    rs.getTimestamp("data_publicacao").toLocalDateTime(),
                    rs.getString("status")
                );
     
                t.setNomeUsuario(rs.getString("nome"));
                t.setFotoUsuario(rs.getString("foto"));
                topicos.add(t);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return topicos;
    }
    
    public boolean update(TopicoForum topico) {
        boolean status = false;
        try {  
            String sql = "UPDATE topico_forum SET id_usuario = ?, titulo = ?, conteudo = ?, data_publicacao = ?, status = ? WHERE id_topico = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, topico.getIdUsuario());
            st.setString(2, topico.getTitulo());
            st.setString(3, topico.getConteudo());
            st.setTimestamp(4, Timestamp.valueOf(topico.getDataPublicacao()));
            st.setString(5, topico.getStatus());
            st.setInt(6, topico.getIdTopico());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }
    
    public boolean delete(int idTopico) {
        boolean status = false;
        try {  
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM topico_forum WHERE id_topico = " + idTopico);
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }
}
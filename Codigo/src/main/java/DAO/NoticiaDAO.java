package DAO;

import model.Noticia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticiaDAO extends DAO {

    public NoticiaDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public boolean insert(Noticia n) {
        boolean status = false;
        try {
            String sql = "INSERT INTO noticia (titulo, conteudo, fonte, url_imagem, url_noticia, id_usuario) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, n.getTitulo());
            st.setString(2, n.getConteudo());
            st.setString(3, n.getFonte());
            st.setString(4, n.getUrlImagem());
            st.setString(5, n.getUrlNoticia());
            st.setInt(6, n.getIdUsuario());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    public void inserirSeNaoExiste(Noticia n) {
        try {
            String check = "SELECT id_noticia FROM noticia WHERE titulo = ?;";
            PreparedStatement ps = conexao.prepareStatement(check);
            ps.setString(1, n.getTitulo());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) insert(n);
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Noticia> listarTodas() {
        List<Noticia> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM noticia ORDER BY data_publicacao DESC;";
            Statement st = conexao.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
            );
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) lista.add(mapear(rs));
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public Noticia get(int id) {
        Noticia n = null;
        try {
            String sql = "SELECT * FROM noticia WHERE id_noticia = " + id + ";";
            Statement st = conexao.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
            );
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) n = mapear(rs);
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return n;
    }

    public boolean update(Noticia n) {
        boolean status = false;
        try {
            String sql = "UPDATE noticia SET titulo=?, conteudo=?, fonte=?, url_imagem=?, url_noticia=? WHERE id_noticia=?;";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, n.getTitulo());
            st.setString(2, n.getConteudo());
            st.setString(3, n.getFonte());
            st.setString(4, n.getUrlImagem());
            st.setString(5, n.getUrlNoticia());
            st.setInt(6, n.getIdNoticia());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    public boolean delete(int id) {
        boolean status = false;
        try {
            String sql = "DELETE FROM noticia WHERE id_noticia = ?;";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    private Noticia mapear(ResultSet rs) throws SQLException {
        Noticia n = new Noticia();
        n.setIdNoticia(rs.getInt("id_noticia"));
        n.setTitulo(rs.getString("titulo"));
        n.setConteudo(rs.getString("conteudo"));
        n.setFonte(rs.getString("fonte"));
        n.setUrlImagem(rs.getString("url_imagem"));
        n.setUrlNoticia(rs.getString("url_noticia"));
        n.setDataPublicacao(rs.getTimestamp("data_publicacao").toLocalDateTime());
        n.setIdUsuario(rs.getInt("id_usuario"));
        return n;
    }
}
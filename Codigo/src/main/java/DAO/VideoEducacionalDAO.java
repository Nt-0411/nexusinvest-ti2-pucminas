package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.VideoEducacional;

public class VideoEducacionalDAO extends DAO {

    public VideoEducacionalDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public boolean insert(VideoEducacional v) {
        boolean status = false;
        try {
            String sql = "INSERT INTO video_educacional (titulo, descricao, categoria, duracao, url_youtube) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, v.getTitulo());
            st.setString(2, v.getDescricao());
            st.setString(3, v.getCategoria());
            st.setInt(4, v.getDuracao());
            st.setString(5, v.getUrlYoutube());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public VideoEducacional get(int idVideo) {
        VideoEducacional v = null;
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM video_educacional WHERE id_video = " + idVideo;
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                v = new VideoEducacional();
                v.setIdVideo(rs.getInt("id_video"));
                v.setTitulo(rs.getString("titulo"));
                v.setDescricao(rs.getString("descricao"));
                v.setCategoria(rs.getString("categoria"));
                v.setDuracao(rs.getInt("duracao"));
                v.setUrlYoutube(rs.getString("url_youtube"));
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return v;
    }

    public List<VideoEducacional> get() {
        List<VideoEducacional> lista = new ArrayList<>();
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM video_educacional";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                VideoEducacional v = new VideoEducacional();
                v.setIdVideo(rs.getInt("id_video"));
                v.setTitulo(rs.getString("titulo"));
                v.setDescricao(rs.getString("descricao"));
                v.setCategoria(rs.getString("categoria"));
                v.setDuracao(rs.getInt("duracao"));
                v.setUrlYoutube(rs.getString("url_youtube"));
                lista.add(v);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lista;
    }

    public boolean update(VideoEducacional v) {
        boolean status = false;
        try {
            String sql = "UPDATE video_educacional SET titulo=?, descricao=?, categoria=?, duracao=?, url_youtube=? WHERE id_video=?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, v.getTitulo());
            st.setString(2, v.getDescricao());
            st.setString(3, v.getCategoria());
            st.setInt(4, v.getDuracao());
            st.setString(5, v.getUrlYoutube());
            st.setInt(6, v.getIdVideo());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean delete(int idVideo) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM video_educacional WHERE id_video = " + idVideo);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
}

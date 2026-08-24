package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.UsuarioAssisteVideo;

public class UsuarioAssisteVideoDAO extends DAO {

    public UsuarioAssisteVideoDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public boolean insert(UsuarioAssisteVideo uav) {
        boolean status = false;
        try {
            String sql = "INSERT INTO usuario_assiste_video (id_usuario, id_video, status) VALUES (?, ?, ?)";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, uav.getIdUsuario());
            st.setInt(2, uav.getIdVideo());
            st.setString(3, uav.getStatus());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean updateStatus(int idUsuario, int idVideo, String novoStatus) {
        boolean status = false;
        try {
            String sql = "UPDATE usuario_assiste_video SET status = '" + novoStatus + "' WHERE id_usuario = " + idUsuario + " AND id_video = " + idVideo;
            Statement st = conexao.createStatement();
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public List<UsuarioAssisteVideo> getByUsuario(int idUsuario) {
        List<UsuarioAssisteVideo> lista = new ArrayList<>();
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM usuario_assiste_video WHERE id_usuario = " + idUsuario;
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                UsuarioAssisteVideo uav = new UsuarioAssisteVideo();
                uav.setIdUsuario(rs.getInt("id_usuario"));
                uav.setIdVideo(rs.getInt("id_video"));
                uav.setStatus(rs.getString("status"));
                lista.add(uav);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lista;
    }
}
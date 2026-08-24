package DAO;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import model.Usuario;

public class UsuarioDAO extends DAO {

    public UsuarioDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    private String md5(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(senha.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashText = number.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insert(Usuario u) {
        boolean status = false;
        try {
            String sql = "INSERT INTO usuario (nome, email, login, senha, foto) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, u.getNome());
            st.setString(2, u.getEmail());
            st.setString(3, u.getLogin());
            st.setString(4, md5(u.getSenha()));
            st.setString(5, u.getFoto());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u2) {
            throw new RuntimeException(u2);
        }
        return status;
    }
    
    public Usuario get(int idUsuario) {
        Usuario u = null;
        try {
            String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
            PreparedStatement st = conexao.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            st.setInt(1, idUsuario);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setIdPerfil(rs.getInt("id_perfil")); 
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                u.setFoto(rs.getString("foto"));
                if (rs.getTimestamp("data_cadastro") != null) {
                    u.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
                }
            }
            st.close();
        } catch (Exception e) {
            System.err.println("Erro ao buscar utilizador: " + e.getMessage());
        }
        return u;
    }

    public boolean updateProfile(Usuario u) {
        boolean status = false;
        try {
            String sql = "UPDATE usuario SET nome = ?, email = ?, foto = ? WHERE id_usuario = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, u.getNome());
            st.setString(2, u.getEmail());
            st.setString(3, u.getFoto());
            st.setInt(4, u.getIdUsuario());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    public boolean updatePassword(int idUsuario, String novaSenha) {
        boolean status = false;
        try {
            String sql = "UPDATE usuario SET senha = ? WHERE id_usuario = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, md5(novaSenha));
            st.setInt(2, idUsuario);
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    public boolean delete(int idUsuario) {
        boolean status = false;
        try {
            String sql = "DELETE FROM usuario WHERE id_usuario = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, idUsuario);
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    public Usuario autenticar(String login, String senha) {
        Usuario u = null;
        try {
            String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?";
            PreparedStatement st = conexao.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            st.setString(1, login);
            st.setString(2, md5(senha)); 
            
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                
                u.setIdPerfil(rs.getInt("id_perfil")); 
                
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setLogin(rs.getString("login"));
                u.setFoto(rs.getString("foto"));
            }
            st.close();
        } catch (Exception e) {
            System.err.println("Erro ao autenticar: " + e.getMessage());
        }
        return u;
    }

    public boolean loginExiste(String login) {
        boolean existe = false;
        try {
            String sql = "SELECT id_usuario FROM usuario WHERE login = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, login);
            ResultSet rs = st.executeQuery();
            existe = rs.next();
            st.close();
        } catch (Exception e) {
            System.err.println("Erro ao verificar login: " + e.getMessage());
        }
        return existe;
    }

    public boolean emailExiste(String email) {
        boolean existe = false;
        try {
            String sql = "SELECT id_usuario FROM usuario WHERE email = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            existe = rs.next();
            st.close();
        } catch (Exception e) {
            System.err.println("Erro ao verificar email: " + e.getMessage());
        }
        return existe;
    }
    
    public boolean updatePerfil(int idUsuario, int idPerfil) {
        boolean status = false;
        try {
            String sql = "UPDATE usuario SET id_perfil = ? WHERE id_usuario = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, idPerfil);
            st.setInt(2, idUsuario);
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }
}
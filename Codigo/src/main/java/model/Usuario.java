package model;

import java.time.LocalDateTime;

public class Usuario {

    private int idUsuario;
    private int idPerfil;
    private String nome;
    private String email;
    private String login;
    private String senha;
    private String foto;
    private LocalDateTime dataCadastro;

    public Usuario() {}

    public Usuario(int idUsuario, int idPerfil, String nome, String email, String login, String senha, String foto, LocalDateTime dataCadastro) {
        this.idUsuario = idUsuario;
        this.idPerfil = idPerfil;
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.foto = foto;
        this.dataCadastro = dataCadastro;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdPerfil() { return idPerfil; }
    public void setIdPerfil(int idPerfil) { this.idPerfil = idPerfil; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}

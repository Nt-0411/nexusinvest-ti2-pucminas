package model;

import java.time.LocalDateTime;

public class TopicoForum {
    private int idTopico;
    private int idUsuario;
    private String titulo;
    private String conteudo;
    private LocalDateTime dataPublicacao;
    private String status;
    private String nomeUsuario;
    private String fotoUsuario;

    public TopicoForum() {
        idTopico = -1;
        idUsuario = -1;
        titulo = "";
        conteudo = "";
        dataPublicacao = LocalDateTime.now(); 
        status = "Ativo"; 
    }

    public TopicoForum(int idTopico, int idUsuario, String titulo, String conteudo, LocalDateTime dataPublicacao, String status) {
        this.idTopico = idTopico;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.dataPublicacao = dataPublicacao;
        this.status = status;
    }

    public int getIdTopico() { return idTopico; }
    public void setIdTopico(int idTopico) { this.idTopico = idTopico; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public LocalDateTime getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDateTime dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getFotoUsuario() { return fotoUsuario; }
    public void setFotoUsuario(String fotoUsuario) { this.fotoUsuario = fotoUsuario; }

    @Override
    public String toString() {
        return "Topico: " + titulo + " (ID: " + idTopico + ") - Usuario: " + idUsuario + " - Status: " + status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TopicoForum other = (TopicoForum) obj;
        return idTopico == other.idTopico;
    }
}
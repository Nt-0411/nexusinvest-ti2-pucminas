package model;

import java.time.LocalDateTime;

public class MensagemForum {
    private int idMensagem;
    private int idTopico;
    private int idUsuario;
    private String conteudo;
    private LocalDateTime dataPublicacao;
    
    // Campos auxiliares para renderizar no front-end
    private String nomeUsuario;
    private String fotoUsuario;

    public MensagemForum() {}

    public MensagemForum(int idMensagem, int idTopico, int idUsuario, String conteudo, LocalDateTime dataPublicacao) {
        this.idMensagem = idMensagem;
        this.idTopico = idTopico;
        this.idUsuario = idUsuario;
        this.conteudo = conteudo;
        this.dataPublicacao = dataPublicacao;
    }

    // Getters e Setters principais
    public int getIdMensagem() { return idMensagem; }
    public void setIdMensagem(int idMensagem) { this.idMensagem = idMensagem; }

    public int getIdTopico() { return idTopico; }
    public void setIdTopico(int idTopico) { this.idTopico = idTopico; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public LocalDateTime getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDateTime dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    // Getters e Setters auxiliares (JOIN)
    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getFotoUsuario() { return fotoUsuario; }
    public void setFotoUsuario(String fotoUsuario) { this.fotoUsuario = fotoUsuario; }
}
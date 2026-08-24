package model;

import java.time.LocalDateTime;

public class Noticia {

    private int idNoticia;
    private String titulo;
    private String conteudo;
    private String fonte;
    private String urlImagem;
    private String urlNoticia;
    private LocalDateTime dataPublicacao;
    private int idUsuario;

    public Noticia() {}

    public Noticia(int idNoticia, String titulo, String conteudo, String fonte,
                   String urlImagem, String urlNoticia, LocalDateTime dataPublicacao, int idUsuario) {
        this.idNoticia = idNoticia;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.fonte = fonte;
        this.urlImagem = urlImagem;
        this.urlNoticia = urlNoticia;
        this.dataPublicacao = dataPublicacao;
        this.idUsuario = idUsuario;
    }

    public int getIdNoticia() { return idNoticia; }
    public void setIdNoticia(int idNoticia) { this.idNoticia = idNoticia; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public String getFonte() { return fonte; }
    public void setFonte(String fonte) { this.fonte = fonte; }

    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }

    public String getUrlNoticia() { return urlNoticia; }
    public void setUrlNoticia(String urlNoticia) { this.urlNoticia = urlNoticia; }

    public LocalDateTime getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDateTime dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
}
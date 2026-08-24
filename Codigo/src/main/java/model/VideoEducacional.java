package model;

public class VideoEducacional {

    private int idVideo;
    private String titulo;
    private String descricao;
    private String categoria;
    private int duracao;
    private String urlYoutube;

    public VideoEducacional() {}

    public VideoEducacional(int idVideo, String titulo, String descricao, String categoria, int duracao, String urlYoutube) {
        this.idVideo = idVideo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.duracao = duracao;
        this.urlYoutube = urlYoutube;
    }

    public int getIdVideo() { return idVideo; }
    public void setIdVideo(int idVideo) { this.idVideo = idVideo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getDuracao() { return duracao; }
    public void setDuracao(int duracao) { this.duracao = duracao; }

    public String getUrlYoutube() { return urlYoutube; }
    public void setUrlYoutube(String urlYoutube) { this.urlYoutube = urlYoutube; }
}
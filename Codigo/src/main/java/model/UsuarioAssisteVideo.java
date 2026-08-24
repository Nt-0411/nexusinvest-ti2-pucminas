package model;

import java.time.LocalDateTime;

public class UsuarioAssisteVideo {

    private int idUsuario;
    private int idVideo;
    private LocalDateTime dataVisualizacao;
    private String status;

    public UsuarioAssisteVideo() {}

    public UsuarioAssisteVideo(int idUsuario, int idVideo, LocalDateTime dataVisualizacao, String status) {
        this.idUsuario = idUsuario;
        this.idVideo = idVideo;
        this.dataVisualizacao = dataVisualizacao;
        this.status = status;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdVideo() { return idVideo; }
    public void setIdVideo(int idVideo) { this.idVideo = idVideo; }

    public LocalDateTime getDataVisualizacao() { return dataVisualizacao; }
    public void setDataVisualizacao(LocalDateTime dataVisualizacao) { this.dataVisualizacao = dataVisualizacao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
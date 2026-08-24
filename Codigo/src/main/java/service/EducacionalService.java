package service;

import DAO.VideoEducacionalDAO;
import java.io.File;
import model.VideoEducacional;
import java.util.List;
import java.util.Scanner;

// Importações do Spark adicionadas
import spark.Request;
import spark.Response;

public class EducacionalService {

    private VideoEducacionalDAO dao = new VideoEducacionalDAO();

    // Lê o arquivo HTML base
    private String lerTemplate(String nomeArquivo) {
        String form = "";
        try {
            Scanner entrada = new Scanner(new File(nomeArquivo));
            while (entrada.hasNext()) {
                form += (entrada.nextLine() + "\n");
            }
            entrada.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return form;
    }

    // Gera os cards HTML com os vídeos do banco
    public Object listarVideos(Request req, Response res) {
        res.type("text/html; charset=UTF-8");
        try {
            List<VideoEducacional> videos = dao.get();
            StringBuilder cards = new StringBuilder();

            for (VideoEducacional v : videos) {
                String thumbUrl = "https://img.youtube.com/vi/" + v.getUrlYoutube() + "/mqdefault.jpg";
                // Deixei o link relativo (/videos/) para funcionar bem em qualquer lugar
                String linkVideo = "/videos/" + v.getIdVideo();

                cards.append("<div class='tutorial-card'>")
                     .append("<a href='").append(linkVideo).append("' class='video-wrapper'>")
                     .append("<img src='").append(thumbUrl).append("' alt='").append(v.getTitulo()).append("'>")
                     .append("</a>")
                     .append("<div class='card-content'>")
                     .append("<span class='card-categoria'>").append(v.getCategoria()).append("</span>")
                     .append("<h3 class='card-titulo'>").append(v.getTitulo()).append("</h3>")
                     .append("<p class='card-descricao'>").append(v.getDescricao()).append("</p>")
                     .append("</div>")
                     .append("<div class='card-footer'>")
                     .append("<span class='card-duracao'><i class='fa-regular fa-clock'></i> ").append(v.getDuracao()).append(" min</span>")
                     .append("<a href='").append(linkVideo).append("' class='assistir-btn'>Assistir</a>")
                     .append("</div>")
                     .append("</div>");
            }

            String html = lerTemplate("src/main/resources/public/trilha_form.html");
            return html.replace("<CARDS-VIDEOS>", cards.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            return "<h2 style='color:white'>Erro ao carregar vídeos: " + e.getMessage() + "</h2>";
        }
    }
    
    // Detalha o vídeo clicado
    public Object detalharVideo(Request req, Response res) {
        res.type("text/html; charset=UTF-8");
        try {
            int id = Integer.parseInt(req.params("id"));
            VideoEducacional v = dao.get(id);

            if (v == null) {
                return "<h2 style='color:white;padding:40px'>Vídeo não encontrado.</h2>";
            }

            String html = lerTemplate("src/main/resources/public/video_form.html");
            html = html.replace("<VIDEO-TITULO>", v.getTitulo());
            html = html.replace("<VIDEO-URL>", v.getUrlYoutube());
            html = html.replace("<VIDEO-CATEGORIA>", v.getCategoria());
            html = html.replace("<VIDEO-DESCRICAO>", v.getDescricao());

            return html;
            
        } catch (Exception e) {
            e.printStackTrace();
            return "<h2 style='color:white'>Erro ao carregar detalhes: " + e.getMessage() + "</h2>";
        }
    }
}
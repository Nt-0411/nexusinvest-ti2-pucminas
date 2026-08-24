package service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import DAO.MensagemForumDAO;
import DAO.TopicoForumDAO;
import model.TopicoForum;
import spark.Request;
import spark.Response;

public class TopicoForumService {
	
	private ContentSafetyService contentSafety = new ContentSafetyService();
    private TopicoForumDAO topicoDAO = new TopicoForumDAO();
    private MensagemForumDAO mensagemDAO = new MensagemForumDAO();
    private String form;
    private final int FORM_INSERT = 1;
    private final int FORM_DETAIL = 2;
    private final int FORM_UPDATE = 3;
    private final int FORM_ORDERBY_ID = 1;
    private final int FORM_ORDERBY_TITULO = 2;
    private final int FORM_ORDERBY_DATAPUBLICACAO = 3;
    
    public TopicoForumService() {
        makeForm();
    }

    public void makeForm() {
        makeForm(FORM_INSERT, new TopicoForum(), FORM_ORDERBY_TITULO);
    }

    public void makeForm(int orderBy) {
        makeForm(FORM_INSERT, new TopicoForum(), orderBy);
    }

    public void makeForm(int tipo, TopicoForum topico, int orderBy) {
        String nomeArquivo = (tipo == FORM_DETAIL) ? "src/main/resources/public/topic.html" : "src/main/resources/public/forum.html";
        form = "";
        
        try {
            Scanner entrada = new Scanner(new File(nomeArquivo));
            while(entrada.hasNext()){
                form += (entrada.nextLine() + "\n");
            }
            entrada.close();
        } catch (Exception e) { 
            System.out.println(e.getMessage()); 
        }
        
        if (tipo == FORM_DETAIL) {
            
            String fotoCriador = (topico.getFotoUsuario() != null && !topico.getFotoUsuario().trim().isEmpty()) ? topico.getFotoUsuario() : "/assets/images/user_icon.png";
            String nomeCriador = (topico.getNomeUsuario() != null) ? topico.getNomeUsuario() : "Usuário Nexus";

            String umTopico = "<div class=\"topico-head\" style=\"background: var(--input-bg); padding: 2rem; border-radius: 8px; border: 1px solid var(--input-border);\">\n";
            umTopico += "  <div style=\"display: flex; align-items: center; gap: 12px; margin-bottom: 15px;\">\n";
            umTopico += "    <img src=\"" + fotoCriador + "\" style=\"width: 45px; height: 45px; border-radius: 50%; object-fit: cover; border: 2px solid var(--primary-blue);\">\n";
            umTopico += "    <div>\n";
            umTopico += "      <h5 style=\"color: var(--primary-blue); margin: 0; font-weight: 600;\">" + nomeCriador + "</h5>\n";
            umTopico += "      <span style=\"color: var(--text-gray); font-size: 0.8rem;\">Autor do Tópico</span>\n";
            umTopico += "    </div>\n";
            umTopico += "  </div>\n";
            umTopico += "  <h2 style=\"color: var(--text-light); font-weight: 700; margin-bottom: 15px;\">" + topico.getTitulo() + "</h2>\n";
            umTopico += "  <div class=\"text\" style=\"color: var(--text-light); line-height: 1.6; margin-bottom: 15px;\">\n";
            umTopico += "    <p>" + topico.getConteudo() + "</p>\n";
            umTopico += "  </div>\n";
            umTopico += "  <p class=\"data-mensagem\" style=\"color: var(--text-gray); font-size: 0.85rem; margin: 0;\">Publicado em: " + topico.getDataPublicacao().toLocalDate().toString() + "</p>\n";
            umTopico += "  <button onclick=\"document.getElementById('modal-mensagem').style.display='flex'\" class=\"btn btn-primary mt-4\">Responder Tópico</button>\n";
            umTopico += "</div>\n";
            umTopico += "<hr style=\"border-color: var(--input-border); margin: 2.5rem 0;\">\n";
            
            // --- RENDERIZAÇÃO DOS COMENTÁRIOS ---
            umTopico += "<div class=\"comentarios-list\" style=\"width: 100%; max-width: 800px; display: flex; flex-direction: column; gap: 1rem;\">\n";
            umTopico += "  <h4 style=\"color: var(--text-light); margin-bottom: 1rem;\">Comentários:</h4>\n";
            
            List<model.MensagemForum> comentarios = mensagemDAO.getByTopico(topico.getIdTopico());
            
            if (comentarios.isEmpty()) {
                umTopico += "  <p style=\"color: var(--text-gray);\">Seja o primeiro a comentar!</p>\n";
            } else {
                for (model.MensagemForum c : comentarios) {
                    String foto = (c.getFotoUsuario() != null && !c.getFotoUsuario().trim().isEmpty()) ? c.getFotoUsuario() : "/assets/images/user_icon.png";
                    
                    umTopico += "  <div class=\"comentario-card\" data-autor-id=\"" + c.getIdUsuario() + "\" style=\"background: var(--input-bg); padding: 1.5rem; border-radius: 8px; border: 1px solid var(--input-border); margin-bottom: 1rem;\">\n";
                    umTopico += "    <div style=\"display: flex; align-items: center; gap: 10px; margin-bottom: 10px;\">\n";
                    umTopico += "      <img src=\"" + foto + "\" style=\"width: 35px; height: 35px; border-radius: 50%; object-fit: cover;\">\n";
                    umTopico += "      <strong style=\"color: var(--primary-blue);\">" + c.getNomeUsuario() + "</strong>\n";
                    umTopico += "      <span style=\"color: var(--text-gray); font-size: 0.8rem; margin-left: auto;\">" + c.getDataPublicacao().toLocalDate().toString() + "</span>\n";
                    umTopico += "    </div>\n";
                    umTopico += "    <p style=\"color: var(--text-light); margin: 0;\">" + c.getConteudo() + "</p>\n";
                    
                    umTopico += "    <div class=\"acoes-comentario mt-2\" style=\"display: flex; gap: 10px; justify-content: flex-end;\">\n";
                    umTopico += "      <button class=\"btn btn-primary btn-acao-comentario\" style=\"font-size: 0.75rem; padding: 0.2rem 0.6rem;\" onclick=\"editarComentario(" + c.getIdMensagem() + ")\">Editar</button>\n";
                    umTopico += "      <button class=\"btn btn-danger btn-acao-comentario\" style=\"font-size: 0.75rem; padding: 0.2rem 0.6rem; background-color: #dc3545; border: none; color: white;\" onclick=\"excluirComentario(" + c.getIdMensagem() + ")\">Excluir</button>\n";
                    umTopico += "    </div>\n";
                    umTopico += "  </div>\n";
                }
            }
            umTopico += "</div>\n";

            form = form.replaceFirst("<UM-TOPICO>", umTopico);
        }
        if (tipo != FORM_DETAIL) {
            String list = "";
            List<TopicoForum> topicos;
            
            if (orderBy == FORM_ORDERBY_ID) {                   topicos = topicoDAO.getOrderByID();
            } else if (orderBy == FORM_ORDERBY_TITULO) {        topicos = topicoDAO.getOrderByTitulo();
            } else if (orderBy == FORM_ORDERBY_DATAPUBLICACAO) {topicos = topicoDAO.getOrderByDataPublicacao();
            } else {                                            topicos = topicoDAO.get();
            }

            for (TopicoForum t : topicos) {
                String foto = (t.getFotoUsuario() != null && !t.getFotoUsuario().trim().isEmpty()) ? t.getFotoUsuario() : "/assets/images/user_icon.png";
                String nome = (t.getNomeUsuario() != null) ? t.getNomeUsuario() : "Usuário Nexus";

                list += "<div class=\"topicos\" data-autor-id=\"" + t.getIdUsuario() + "\">\n" +
                        "  <div class=\"info-topico\" style=\"align-items: flex-start;\">\n" + 
                        "    <img src=\"" + foto + "\" alt=\"Avatar\" style=\"width: 45px; height: 45px; border-radius: 50%; object-fit: cover; border: 2px solid var(--primary-blue);\">\n" +
                        "    <div class=\"text\">\n" +
                        "      <h6 style=\"color: var(--primary-blue); margin: 0 0 5px 0; font-size: 0.9rem; font-weight: 600;\">" + nome + "</h6>\n" +
                        "      <h3 style=\"margin: 0 0 10px 0;\">" + t.getTitulo() + "</h3>\n" +
                        "      <p>" + t.getConteudo() + "</p>\n" +
                        "    </div>\n" +
                        "    <div class=\"hora-topico\">\n" +
                        "      <p>" + t.getDataPublicacao().toLocalDate().toString() + "</p>\n" +
                        "    </div>\n" +
                        "  </div>\n" +
                        "  <div class=\"buttons-forum\">\n" +
                        "    <a href=\"/topico/" + t.getIdTopico() + "\" class=\"btn\">Detalhar</a>\n" +
                        "    <a href=\"/topico/update/" + t.getIdTopico() + "\" class=\"btn btn-acao-restrita\" style=\"background-color: #3b82f6;\">Editar</a>\n" +
                        "    <a href=\"/topico/delete/" + t.getIdTopico() + "\" class=\"btn btn-excluir btn-acao-restrita\">Excluir</a>\n" +
                        "  </div>\n" +
                        "</div>\n";
            }
            form = form.replaceFirst("<LISTAR-TOPICO>", list);   
            
            String formModal = "";
            if (tipo == FORM_UPDATE) {
                formModal += "<div id=\"modal-editar-topico\" class=\"modal\" style=\"display: flex;\">\n";
                formModal += "  <div class=\"modal-content\">\n";
                formModal += "    <h2>Editar Tópico</h2>\n";
                formModal += "    <form action=\"/topico/update/" + topico.getIdTopico() + "\" method=\"post\">\n";
                formModal += "      <input type=\"text\" name=\"titulo\" value=\"" + topico.getTitulo() + "\" required>\n";
                formModal += "      <textarea name=\"conteudo\" required>" + topico.getConteudo() + "</textarea>\n";
                formModal += "      <div class=\"modal-buttons\">\n";
                formModal += "        <a href=\"/topico/list/1\" class=\"btn-cancelar\" style=\"text-decoration:none; text-align:center;\">Cancelar</a>\n";
                formModal += "        <button type=\"submit\" class=\"btn-criar\">Salvar</button>\n";
                formModal += "      </div>\n";
                formModal += "    </form>\n";
                formModal += "  </div>\n";
                formModal += "</div>\n";
            }
            form = form.replaceFirst("<FORM-MODAL-UPDATE>", formModal);
        }
    }
    
    public Object insert(Request request, Response response) {
        String titulo = request.queryParams("titulo");
        String conteudo = request.queryParams("conteudo");
        String idUsuarioParam = request.queryParams("idUsuario");
        int idUsuario;
        try {
            idUsuario = Integer.parseInt(idUsuarioParam);
        } catch (NumberFormatException e) {
            response.status(401);
            makeForm();
            return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                                     "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"Sessão inválida. Faça login novamente.\">");
        }
        String resp = "";

        // Verifica o título e conteúdo com o Azure Content Safety
        if (!contentSafety.isConteudoSeguro(titulo) || !contentSafety.isConteudoSeguro(conteudo)) {
            resp = "Conteúdo bloqueado: texto contém conteúdo inapropriado.";
            makeForm();
            return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                                     "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }

        TopicoForum topico = new TopicoForum(-1, idUsuario, titulo, conteudo, LocalDateTime.now(), "Ativo");

        if (topicoDAO.insert(topico)) {
            resp = "Tópico inserido com sucesso!";
            response.status(201);
        } else {
            resp = "Erro ao inserir tópico.";
            response.status(404);
        }

        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
                                 "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));        
        TopicoForum topico = (TopicoForum) topicoDAO.get(id);
        
        if (topico != null) {
            response.status(200); 
            makeForm(FORM_DETAIL, topico, FORM_ORDERBY_TITULO);
        } else {
            response.status(404); 
            makeForm();
        }
        return form;
    }

    public Object getToUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));        
        TopicoForum topico = (TopicoForum) topicoDAO.get(id);
        
        if (topico != null) {
            response.status(200); 
            makeForm(FORM_UPDATE, topico, FORM_ORDERBY_TITULO);
        } else {
            response.status(404); 
            makeForm();
        }
        return form;
    }
    
    public Object getAll(Request request, Response response) {
        int orderBy = Integer.parseInt(request.params(":orderby"));
        makeForm(orderBy);
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return form;
    }            
    
    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        TopicoForum topico = topicoDAO.get(id);
        String resp = "";       

        if (topico != null) {
            topico.setTitulo(request.queryParams("titulo"));
            topico.setConteudo(request.queryParams("conteudo"));
            topicoDAO.update(topico);
            response.status(200); 
            resp = "Tópico atualizado!";
        } else {
            response.status(404); 
            resp = "Tópico não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", 
                                 "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }

    public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        TopicoForum topico = topicoDAO.get(id);
        String resp = "";       

        if (topico != null) {
            topicoDAO.delete(id);
            response.status(200); 
            resp = "Tópico excluído!";
        } else {
            response.status(404); 
            resp = "Tópico não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", 
                                 "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
    }
    
    public String inserirMensagem(Request req, Response res) {
        res.type("application/json");
        try {
            int idTopico = Integer.parseInt(req.params(":idTopico"));
            
            com.google.gson.JsonObject body = new com.google.gson.Gson().fromJson(req.body(), com.google.gson.JsonObject.class);
            int idUsuario = body.get("idUsuario").getAsInt();
            String conteudo = body.get("conteudo").getAsString();

            // Verifica o conteúdo com o Azure Content Safety
            if (!contentSafety.isConteudoSeguro(conteudo)) {
                res.status(400);
                return "{\"sucesso\": false, \"mensagem\": \"Conteúdo bloqueado: texto contém conteúdo inapropriado.\"}";
            }
            
            model.MensagemForum msg = new model.MensagemForum(-1, idTopico, idUsuario, conteudo, LocalDateTime.now());
            
            if(mensagemDAO.insert(msg)) {
                return "{\"sucesso\": true}";
            } else {
                res.status(500);
                return "{\"sucesso\": false, \"mensagem\": \"Erro ao salvar comentário.\"}";
            }
        } catch (Exception e) {
            res.status(500);
            return "{\"sucesso\": false, \"mensagem\": \"" + e.getMessage() + "\"}";
        }
    }

    public String updateMensagem(Request req, Response res) {
        res.type("application/json");
        try {
            int idMensagem = Integer.parseInt(req.params(":idMensagem"));
            
            com.google.gson.JsonObject body = new com.google.gson.Gson().fromJson(req.body(), com.google.gson.JsonObject.class);
            String conteudo = body.get("conteudo").getAsString();
            
            model.MensagemForum msg = mensagemDAO.get(idMensagem);
            if (msg != null) {
                msg.setConteudo(conteudo);
                if (mensagemDAO.update(msg)) {
                    return "{\"sucesso\": true}";
                }
            }
            res.status(500);
            return "{\"sucesso\": false, \"mensagem\": \"Erro ao atualizar comentário no banco.\"}";
        } catch (Exception e) {
            res.status(500);
            return "{\"sucesso\": false, \"mensagem\": \"" + e.getMessage() + "\"}";
        }
    }

    public String deleteMensagem(Request req, Response res) {
        res.type("application/json");
        try {
            int idMensagem = Integer.parseInt(req.params(":idMensagem"));
            
            if (mensagemDAO.delete(idMensagem)) {
                return "{\"sucesso\": true}";
            } else {
                res.status(500);
                return "{\"sucesso\": false, \"mensagem\": \"Erro ao excluir comentário.\"}";
            }
        } catch (Exception e) {
            res.status(500);
            return "{\"sucesso\": false, \"mensagem\": \"" + e.getMessage() + "\"}";
        }
    }
}
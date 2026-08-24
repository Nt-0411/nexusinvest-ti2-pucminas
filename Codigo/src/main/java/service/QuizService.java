package service;

import DAO.UsuarioDAO;
import spark.Request;
import spark.Response;

public class QuizService {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public String calcularPerfil(Request req, Response res) {
        res.type("application/json");
        int idUsuario = Integer.parseInt(req.params(":id"));
        
        try {
            com.google.gson.JsonObject body = new com.google.gson.Gson().fromJson(req.body(), com.google.gson.JsonObject.class);
            int pontuacao = body.get("pontuacao").getAsInt();
            
            int idPerfil;
            String nomePerfil;

            if (pontuacao <= 23) {
                idPerfil = 1; 
                nomePerfil = "Conservador";
            } else if (pontuacao <= 36) {
                idPerfil = 2;
                nomePerfil = "Moderado";
            } else {
                idPerfil = 3; 
                nomePerfil = "Arrojado";
            }

            if (usuarioDAO.updatePerfil(idUsuario, idPerfil)) {
                return "{\"sucesso\": true, \"idPerfil\": " + idPerfil + ", \"perfil\": \"" + nomePerfil + "\"}";
            } else {
                res.status(500);
                return "{\"sucesso\": false, \"mensagem\": \"Erro ao atualizar o banco.\"}";
            }
        } catch (Exception e) {
            res.status(400);
            return "{\"sucesso\": false, \"mensagem\": \"Dados inválidos.\"}";
        }
    }
}
package service;

import DAO.UsuarioDAO;
import model.Usuario;
import spark.Request;
import spark.Response;

public class ConfiguracaoService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public ConfiguracaoService() {
    }
    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Usuario usuario = usuarioDAO.get(id);
        
        if (usuario != null) {
            String body = request.body();
            
            if (body.contains("\"senha\"")) {
                String novaSenha = extractJsonValue(body, "senha");
                usuario.setSenha(novaSenha);
                
                if (usuarioDAO.updatePassword(usuario.getIdUsuario(), novaSenha)) { 
                    response.status(200);
                    return "{\"message\": \"Senha atualizada com sucesso!\"}";
                }
            } else {
                String novoNome = extractJsonValue(body, "nome");
                String novoEmail = extractJsonValue(body, "email");
                String novaFoto = extractJsonValue(body, "foto");
                
                if (novoNome != null && !novoNome.isEmpty()) usuario.setNome(novoNome);
                if (novoEmail != null && !novoEmail.isEmpty()) usuario.setEmail(novoEmail);
                if (novaFoto != null && !novaFoto.isEmpty()) usuario.setFoto(novaFoto);
                
                if(usuarioDAO.updateProfile(usuario)) {
                    response.status(200);
                    return "{\"message\": \"Perfil atualizado com sucesso!\"}";
                }
            }
            
            response.status(500);
            return "{\"message\": \"Erro ao atualizar banco de dados.\"}";
        } else {
            response.status(404);
            return "{\"message\": \"Usuário não encontrado.\"}";
        }
    }

    public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Usuario usuario = usuarioDAO.get(id);

        if (usuario != null) {
            if(usuarioDAO.delete(id)) {
                response.status(200);
                return "{\"message\": \"Conta excluída com sucesso!\"}";
            } else {
                response.status(500);
                return "{\"message\": \"Erro ao excluir a conta.\"}";
            }
        } else {
            response.status(404);
            return "{\"message\": \"Usuário não encontrado.\"}";
        }
    }
    
    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return null;
        
        startIndex += searchKey.length();
        int endIndex;
        
        if (json.charAt(startIndex) == '"' || json.charAt(startIndex) == ' ') {
            startIndex = json.indexOf('"', startIndex) + 1;
            endIndex = json.indexOf('"', startIndex);
        } else {
            endIndex = json.indexOf(',', startIndex);
            if (endIndex == -1) endIndex = json.indexOf('}', startIndex);
        }
        
        return json.substring(startIndex, endIndex).trim();
    }
}
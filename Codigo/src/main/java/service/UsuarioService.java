package service;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

import DAO.UsuarioDAO;
import model.Usuario;
import spark.Request;
import spark.Response;

public class UsuarioService {

    private UsuarioDAO dao = new UsuarioDAO();
    private Gson gson = new Gson();

    private void antiCache(Response res) {
        res.header("Cache-Control", "no-cache, no-store, must-revalidate");
        res.header("Pragma", "no-cache");
        res.header("Expires", "0");
    }

    private String lerTemplate() {
        String form = "";
        try {
            Scanner entrada = new Scanner(new File("src/main/resources/public/usuario_form.html"));
            while (entrada.hasNext()) {
                form += (entrada.nextLine() + "\n");
            }
            entrada.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return form;
    }

    public String getLoginForm(Request req, Response res) {
        antiCache(res);
        res.type("text/html; charset=UTF-8");
        String html = lerTemplate();

        String formLogin = "<div id='login-container'>" +
            "<div class='text-center mb-4'>" +
            "<h3 class='brand-title'><i class='bi bi-graph-up-arrow'></i> Nexus Invest</h3>" +
            "<p class='brand-subtitle'>Identificação de Usuário</p>" +
            "</div>" +
            "<div id='alerta-login' class='alert-erro'></div>" +
            "<form id='login-form'>" +
            "<div class='mb-3'><label class='form-label'>Login</label>" +
            "<input type='text' id='username' class='form-control' placeholder='Digite seu usuário'></div>" +
            "<div class='mb-3'><label class='form-label'>Senha</label>" +
            "<input type='password' id='password' class='form-control' placeholder='Digite sua senha'></div>" +
            "<div class='text-center mt-3'>" +
            "<button type='submit' class='btn btn-primary'>Login</button>" +
            "<div class='mt-3'><span style='color:#888;font-size:0.9rem'>Não possui conta?</span><br>" +
            "<button type='button' class='btn btn-link text-info' onclick='toggleScreens()'>Registrar</button>" +
            "</div></div></form></div>";

        return html.replace("<FORM-LOGIN>", formLogin);
    }

    public String autenticar(Request req, Response res) {
        antiCache(res);
        res.type("application/json");
        Map<String, Object> resultado = new HashMap<>();
        try {
            String login = req.queryParams("login");
            String senha = req.queryParams("senha");

            Usuario u = dao.autenticar(login, senha);

            if (u != null) {
                resultado.put("sucesso", true);
                resultado.put("usuario", u);
            } else {
                resultado.put("sucesso", false);
                resultado.put("mensagem", "Usuário ou senha incorretos");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultado.put("sucesso", false);
            resultado.put("mensagem", e.getMessage());
        }
        return gson.toJson(resultado);
    }

    public String cadastrar(Request req, Response res) {
        antiCache(res);
        res.type("application/json");
        Map<String, Object> resultado = new HashMap<>();

        try {
            com.google.gson.JsonObject body = gson.fromJson(req.body(), com.google.gson.JsonObject.class);
            String login = body.get("login").getAsString();
            String nome = body.get("nome").getAsString();
            String email = body.get("email").getAsString();
            String senha = body.get("senha").getAsString();
            String foto = body.has("foto") && !body.get("foto").getAsString().isEmpty() 
                          ? body.get("foto").getAsString() 
                          : "/assets/images/user_icon.png";

            if (dao.loginExiste(login)) {
                resultado.put("sucesso", false);
                resultado.put("mensagem", "Login já está em uso!");
                return gson.toJson(resultado);
            }

            if (dao.emailExiste(email)) {
                resultado.put("sucesso", false);
                resultado.put("mensagem", "Este e-mail já está registado num utilizador!");
                return gson.toJson(resultado);
            }

            Usuario u = new Usuario();
            u.setLogin(login);
            u.setNome(nome);
            u.setEmail(email);
            u.setSenha(senha);
            u.setFoto(foto);

            dao.insert(u);
            resultado.put("sucesso", true);
            resultado.put("mensagem", "Usuário cadastrado com sucesso!");

        } catch (Exception e) {
            resultado.put("sucesso", false);
            resultado.put("mensagem", "Erro interno: " + e.getMessage());
        }

        return gson.toJson(resultado);
    }

    public String atualizar(Request req, Response res) {
        antiCache(res);
        res.type("application/json");
        Map<String, Object> resultado = new HashMap<>();
        
        try {
            int id = Integer.parseInt(req.params(":id"));
            Usuario u = dao.get(id);
            
            if (u == null) {
                res.status(404);
                resultado.put("sucesso", false);
                resultado.put("mensagem", "Usuário não encontrado.");
                return gson.toJson(resultado);
            }

            com.google.gson.JsonObject body = gson.fromJson(req.body(), com.google.gson.JsonObject.class);
            
            if (body.has("senha")) {
                String novaSenha = body.get("senha").getAsString();
                if (dao.updatePassword(id, novaSenha)) {
                    resultado.put("sucesso", true);
                    resultado.put("mensagem", "Senha atualizada com sucesso!");
                }
            } else if (body.has("id_perfil")) {
                int idPerfil = body.get("id_perfil").getAsInt();
                u.setIdPerfil(idPerfil);
                
                if (dao.updatePerfil(id, idPerfil)) {
                    resultado.put("sucesso", true);
                    resultado.put("mensagem", "Perfil de investidor salvo com sucesso!");
                } else {
                    resultado.put("sucesso", false);
                    resultado.put("mensagem", "Erro ao atualizar o perfil de investidor no banco.");
                }
            } else {
                if (body.has("nome")) u.setNome(body.get("nome").getAsString());
                if (body.has("email")) u.setEmail(body.get("email").getAsString());
                if (body.has("foto")) u.setFoto(body.get("foto").getAsString());
                
                if (dao.updateProfile(u)) {
                    resultado.put("sucesso", true);
                    resultado.put("mensagem", "Perfil atualizado com sucesso!");
                }
            }
        } catch (Exception e) {
            res.status(500);
            resultado.put("sucesso", false);
            resultado.put("mensagem", "Erro interno: " + e.getMessage());
        }
        return gson.toJson(resultado);
    }

    public String deletar(Request req, Response res) {
        antiCache(res);
        res.type("application/json");
        Map<String, Object> resultado = new HashMap<>();
        
        try {
            int id = Integer.parseInt(req.params(":id"));
            if (dao.delete(id)) {
                resultado.put("sucesso", true);
                resultado.put("mensagem", "Conta excluída com sucesso!");
            } else {
                res.status(500);
                resultado.put("sucesso", false);
                resultado.put("mensagem", "Erro ao excluir conta no banco.");
            }
        } catch (Exception e) {
            res.status(500);
            resultado.put("sucesso", false);
            resultado.put("mensagem", "Erro interno: " + e.getMessage());
        }
        return gson.toJson(resultado);
    }
}
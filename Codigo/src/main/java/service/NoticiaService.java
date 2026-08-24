package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import DAO.NoticiaDAO;
import model.Noticia;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

public class NoticiaService {
    private NoticiaDAO dao = new NoticiaDAO();
    private NoticiaApiService apiService = new NoticiaApiService();


    private Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.toString());
            }
        })
        .create();

    public String getAll(Request req, Response res) throws Exception {
        res.type("application/json");
        apiService.sincronizarNoticias();
        List<Noticia> noticias = dao.listarTodas();
        return gson.toJson(noticias);
    }

    public String inserir(Request req, Response res) throws Exception {
        res.type("application/json");
        Noticia n = gson.fromJson(req.body(), Noticia.class);
        boolean ok = dao.insert(n);
        if (ok) { res.status(201); return "{\"mensagem\":\"Notícia criada!\"}"; }
        res.status(500);
        return "{\"erro\":\"Erro ao criar notícia.\"}";
    }

    public String deletar(Request req, Response res) throws Exception {
        res.type("application/json");
        int id = Integer.parseInt(req.params("id"));
        boolean ok = dao.delete(id);
        if (ok) return "{\"mensagem\":\"Notícia deletada!\"}";
        res.status(404);
        return "{\"erro\":\"Notícia não encontrada.\"}";
    }
}
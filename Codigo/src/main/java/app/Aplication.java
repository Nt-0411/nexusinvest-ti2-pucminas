package app;

import static spark.Spark.*;
import service.QuizService;
import java.nio.file.Files;
import java.nio.file.Paths;

import service.MetasService;
import service.TopicoForumService;
import service.SimulacaoService;
import service.EducacionalService;
import service.UsuarioService;
import service.NoticiaService;
import service.DashboardService;


public class Aplication {
    
    private static MetasService metasService             = new MetasService();
    private static TopicoForumService topicoService       = new TopicoForumService();
    private static SimulacaoService simService            = new SimulacaoService();
    private static EducacionalService educacionalService  = new EducacionalService();
    private static UsuarioService usuarioService          = new UsuarioService();
    private static NoticiaService noticiaService    = new NoticiaService();
    private static DashboardService dashboardService = new DashboardService();
    private static QuizService quizService = new QuizService();

    public static void main(String[] args) {
        port(Config.getInt("NEXUS_PORT", 6789));
        staticFiles.location("/public");

        // Entrega ao front-end as configurações públicas (token da brapi.dev),
        // que ficam fora do código-fonte. Ver Config.java e o .env.example.
        get("/config.js", (req, res) -> {
            res.type("application/javascript; charset=UTF-8");
            return "window.NEXUS_CONFIG = { brapiToken: \""
                 + Config.get("NEXUS_BRAPI_TOKEN", "") + "\" };";
        });

        // --- ROTAS DA EQUIPE ---
        get("/metas",               (req, res) -> { res.redirect("/metas/list/1"); return ""; });
        post("/metas/insert",       (req, res) -> metasService.insert(req, res));
        get("/metas/list/:orderby", (req, res) -> metasService.getAll(req, res));
        get("/metas/update/:id",    (req, res) -> metasService.getToUpdate(req, res));
        post("/metas/update/:id",   (req, res) -> metasService.update(req, res));
        post("/metas/aporte/:id",   (req, res) -> metasService.aporte(req, res));
        get("/metas/delete/:id",    (req, res) -> metasService.delete(req, res));

        get("/topico",                  (req, res) -> { res.redirect("/topico/list/1"); return ""; });
        post("/topico/insert",          (req, res) -> topicoService.insert(req, res));
        get("/topico/:id",              (req, res) -> topicoService.get(req, res));
        get("/topico/list/:orderby",    (req, res) -> topicoService.getAll(req, res));
        get("/topico/update/:id",       (req, res) -> topicoService.getToUpdate(req, res));
        post("/topico/update/:id",      (req, res) -> topicoService.update(req, res));
        get("/topico/delete/:id",       (req, res) -> topicoService.delete(req, res));
        post("/api/topico/:idTopico/mensagem", (req, res) -> topicoService.inserirMensagem(req, res));
        put("/api/mensagem/:idMensagem",    (req, res) -> topicoService.updateMensagem(req, res));
        delete("/api/mensagem/:idMensagem", (req, res) -> topicoService.deleteMensagem(req, res));
        
        get("/simulation",                  (req, res) -> simService.index(req, res));
        post("/simulation/depositar",       (req, res) -> simService.depositar(req, res));
        post("/simulation/comprar",         (req, res) -> simService.comprar(req, res));
        get("/simulation/vender/:id_item",  (req, res) -> simService.vender(req, res));

        get("/login",               (req, res) -> usuarioService.getLoginForm(req, res));
        post("/login",              (req, res) -> usuarioService.autenticar(req, res));
        post("/usuario/cadastrar",  (req, res) -> usuarioService.cadastrar(req, res));
        patch("/usuario/:id",       (req, res) -> usuarioService.atualizar(req, res));
        delete("/usuario/:id",      (req, res) -> usuarioService.deletar(req, res));
        
        get("/videos",              (req, res) -> educacionalService.listarVideos(req, res));
        get("/tutorials",           (req, res) -> educacionalService.listarVideos(req, res));  
        get("/videos/:id",          (req, res) -> educacionalService.detalharVideo(req, res));
        get("/tutorials/:id",       (req, res) -> educacionalService.detalharVideo(req, res));
        
        get("/dashboard",            (req, res) -> dashboardService.getDashboard(req, res));
        
        get("/settings", (req, res) -> {
            res.type("text/html; charset=UTF-8");
            return new String(Files.readAllBytes(Paths.get("src/main/resources/public/settings.html")));
        });
        
        get("/noticias", (req, res) -> {
            res.type("text/html");
            return new String(Files.readAllBytes(Paths.get("src/main/resources/public/noticias.html")));
        });
        
        get("/api/noticias", (req, res) -> noticiaService.getAll(req, res));
        post("/noticias/nova", (req, res) -> noticiaService.inserir(req, res));
        delete("/noticias/:id", (req, res) -> noticiaService.deletar(req, res));
        
        post("/api/quiz/resultado/:id", (req, res) -> quizService.calcularPerfil(req, res));
    
    }
}
package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import DAO.NoticiaDAO;
import model.Noticia;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NoticiaApiService {

    // Chave configurável por variável de ambiente ou .env — ver Config.java
    private static final String API_KEY = app.Config.get("NEXUS_NEWSDATA_API_KEY", "");
    private static final String API_URL = "https://newsdata.io/api/1/latest?apikey="
            + API_KEY + "&q=mercado%20financeiro&language=pt";

    private NoticiaDAO dao = new NoticiaDAO();

    public void sincronizarNoticias() {
        // Sem chave configurada não há como consultar a API; mantém o que já está no banco.
        if (API_KEY.isEmpty()) {
            System.err.println("Sincronização de notícias desativada: defina NEXUS_NEWSDATA_API_KEY.");
            return;
        }

        try {
            // Usando HttpURLConnection — já vem no Java, sem dependência extra!
            URL url = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);

            BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream())
            );
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            String json = sb.toString();
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            JsonArray results = obj.getAsJsonArray("results");

            if (results == null || results.size() == 0) {
                System.out.println("Nenhuma notícia encontrada na API.");
                return;
            }

            for (int i = 0; i < results.size(); i++) {
                JsonObject item = results.get(i).getAsJsonObject();

                String titulo = item.has("title") && !item.get("title").isJsonNull()
                        ? item.get("title").getAsString() : null;
                String conteudo = item.has("description") && !item.get("description").isJsonNull()
                        ? item.get("description").getAsString() : "Sem descrição.";
                String fonte = item.has("source_id") && !item.get("source_id").isJsonNull()
                        ? item.get("source_id").getAsString() : "Desconhecida";
                String urlImagem = item.has("image_url") && !item.get("image_url").isJsonNull()
                        ? item.get("image_url").getAsString() : null;
                String urlNoticia = item.has("link") && !item.get("link").isJsonNull()
                        ? item.get("link").getAsString() : null;

                if (titulo == null) continue;

                Noticia n = new Noticia();
                n.setTitulo(titulo);
                n.setConteudo(conteudo);
                n.setFonte(fonte);
                n.setUrlNoticia(urlNoticia);
                n.setUrlImagem(urlImagem);
                n.setIdUsuario(1);

                dao.inserirSeNaoExiste(n);
            }
            System.out.println("Sincronização concluída!");

        } catch (Exception e) {
            System.err.println("Erro na sincronização: " + e.getMessage());
        }
    }
}
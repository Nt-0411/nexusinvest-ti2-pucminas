package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import app.Config;

public class ContentSafetyService {

    // Configuráveis por variável de ambiente ou .env — ver Config.java
    private static final String ENDPOINT = Config.get("NEXUS_AZURE_CONTENT_SAFETY_ENDPOINT", "");
    private static final String KEY      = Config.get("NEXUS_AZURE_CONTENT_SAFETY_KEY", "");

    public boolean isConteudoSeguro(String texto) {
        // Sem credencial configurada a moderação fica desligada e o conteúdo passa.
        if (ENDPOINT.isEmpty() || KEY.isEmpty()) {
            System.err.println("Content Safety desativado: defina NEXUS_AZURE_CONTENT_SAFETY_ENDPOINT e NEXUS_AZURE_CONTENT_SAFETY_KEY.");
            return true;
        }

        try {
            String url = ENDPOINT + "contentsafety/text:analyze?api-version=2023-10-01";

            String body = "{\"text\": \"" + texto.replace("\"", "\\\"").replace("\n", " ") + "\", \"categories\": [\"Hate\", \"SelfHarm\", \"Sexual\", \"Violence\"], \"outputType\": \"FourSeverityLevels\"}";

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Ocp-Apim-Subscription-Key", KEY);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes("UTF-8"));
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            String response = sb.toString();
            System.out.println("Content Safety Response: " + response);

            // Se qualquer categoria tiver severidade > 0, bloqueia
            return !response.matches(".*\"severity\":[1-9].*");

        } catch (Exception e) {
            System.err.println("Erro ao chamar Content Safety: " + e.getMessage());
            return true;
        }
    }
}
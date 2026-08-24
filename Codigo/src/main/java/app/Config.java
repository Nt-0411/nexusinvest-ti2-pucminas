package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Leitura centralizada das configuracoes sensiveis (credenciais de banco e
 * chaves de API). Nenhum segredo fica escrito no codigo-fonte.
 *
 * A busca acontece nesta ordem, parando no primeiro valor encontrado:
 *   1. variavel de ambiente        (ex.: NEXUS_DB_PASSWORD)
 *   2. propriedade da JVM          (ex.: -DNEXUS_DB_PASSWORD=...)
 *   3. arquivo .env na raiz do projeto (nao versionado)
 *   4. valor padrao informado na chamada
 */
public class Config {

    private static Map<String, String> arquivoEnv = null;

    public static String get(String chave, String padrao) {
        String valor = System.getenv(chave);
        if (valor != null && !valor.trim().isEmpty()) return valor.trim();

        valor = System.getProperty(chave);
        if (valor != null && !valor.trim().isEmpty()) return valor.trim();

        valor = lerDoArquivoEnv(chave);
        if (valor != null && !valor.trim().isEmpty()) return valor.trim();

        return padrao;
    }

    public static int getInt(String chave, int padrao) {
        try {
            return Integer.parseInt(get(chave, String.valueOf(padrao)));
        } catch (NumberFormatException e) {
            return padrao;
        }
    }

    private static synchronized String lerDoArquivoEnv(String chave) {
        if (arquivoEnv == null) {
            arquivoEnv = new HashMap<String, String>();
            carregarArquivoEnv();
        }
        return arquivoEnv.get(chave);
    }

    private static void carregarArquivoEnv() {
        File env = new File(".env");
        if (!env.exists()) env = new File("Codigo/.env");
        if (!env.exists()) return;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(env));
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("#")) continue;

                int igual = linha.indexOf('=');
                if (igual <= 0) continue;

                String nome = linha.substring(0, igual).trim();
                String valor = linha.substring(igual + 1).trim();

                // remove aspas envolvendo o valor, se houver
                if (valor.length() >= 2
                        && ((valor.startsWith("\"") && valor.endsWith("\""))
                         || (valor.startsWith("'")  && valor.endsWith("'")))) {
                    valor = valor.substring(1, valor.length() - 1);
                }
                arquivoEnv.put(nome, valor);
            }
        } catch (Exception e) {
            System.err.println("Nao foi possivel ler o arquivo .env -- " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); } catch (Exception ignorada) { }
        }
    }
}

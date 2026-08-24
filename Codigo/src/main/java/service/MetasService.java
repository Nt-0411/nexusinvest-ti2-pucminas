package service;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import DAO.MetasDao;
import model.Metas;
import spark.Request;
import spark.Response;

public class MetasService {

    private MetasDao metasDao = new MetasDao();
    private String form;

    private final int FORM_INSERT      = 1;
    private final int FORM_UPDATE      = 3;
    private final int FORM_ORDERBY_ID  = 1;
    private final int FORM_ORDERBY_NOME  = 2;
    private final int FORM_ORDERBY_VALOR = 3;

    public MetasService() {
        makeForm();
    }

    private String fmt(double valor) {
        return String.format("%.2f", valor);
    }


    private String validarCampos(String nome,
                                  String valorTotalStr,
                                  String valorAlcancadoStr,
                                  String dataPrazoStr,
                                  boolean isInsert) {

        if (nome == null || nome.trim().isEmpty()) {
            return "Erro: O nome da meta não pode estar vazio.";
        }


        double valorTotal;
        try {
            valorTotal = Double.parseDouble(valorTotalStr.replace(".", "").replace(",", "."));
        } catch (NumberFormatException e) {
            return "Erro: Valor Total inválido. Digite apenas números.";
        }
        if (valorTotal <= 0) {
            return "Erro: O Valor Total deve ser maior que zero.";
        }

        double valorAlcancado;
        try {
            valorAlcancado = Double.parseDouble(valorAlcancadoStr.replace(",", "."));
        } catch (NumberFormatException e) {
            return "Erro: Valor Atual inválido. Digite apenas números.";
        }
        if (valorAlcancado < 0) {
            return "Erro: O Valor Atual não pode ser negativo.";
        }
        if (valorAlcancado > valorTotal) {
            return "Erro: O Valor Atual (R$ " + fmt(valorAlcancado) +
                   ") não pode ser maior que o Valor Total (R$ " + fmt(valorTotal) + ").";
        }

        if (dataPrazoStr == null || dataPrazoStr.trim().isEmpty()) {
            return "Erro: Informe uma data de prazo.";
        }
        LocalDate dataPrazo;
        try {
            dataPrazo = LocalDate.parse(dataPrazoStr.trim());
        } catch (Exception e) {
            return "Erro: Data de prazo inválida. Use o formato AAAA-MM-DD.";
        }


        if (!dataPrazo.isAfter(LocalDate.now())) {
            return "Erro: O prazo deve ser uma data futura (posterior a hoje, " +
                   LocalDate.now() + ").";
        }

        return ""; 
    }


    public void makeForm() {
        makeForm(FORM_INSERT, new Metas(), FORM_ORDERBY_NOME);
    }

    public void makeForm(int orderBy) {
        makeForm(FORM_INSERT, new Metas(), orderBy);
    }

    public void makeForm(int tipo, Metas meta, int orderBy) {
        String nomeArquivo = "src/main/resources/public/metas.html";
        form = "";
        try {
            Scanner entrada = new Scanner(new File(nomeArquivo));
            while (entrada.hasNext()) {
                form += (entrada.nextLine() + "\n");
            }
            entrada.close();
        } catch (Exception e) {
            System.out.println("Erro ao ler o HTML: " + e.getMessage());
        }


        String umaMeta = "";

        if (tipo != FORM_INSERT) {
            umaMeta += "<div class=\"form-header\">";
            umaMeta += "  <a href=\"/metas\" class=\"btn-link btn-back\">"
                     + "<i class=\"fa-solid fa-arrow-left\"></i> Voltar / Nova Meta</a>";
            umaMeta += "</div>";
        }

        if (tipo == FORM_INSERT || tipo == FORM_UPDATE) {
            String action, title, buttonLabel;

            if (tipo == FORM_INSERT) {
                action      = "/metas/insert";
                title       = "NOVA META FINANCEIRA";
                buttonLabel = "SALVAR META";
            } else {
                action      = "/metas/update/" + meta.getId_meta();
                title       = "ATUALIZAR META (ID " + meta.getId_meta() + ")";
                buttonLabel = "ATUALIZAR";
            }

            umaMeta += "<div id=\"page-form\" class=\"page active\">";
            umaMeta += "  <div class=\"form-header\"><h2>" + title + "</h2></div>";
            umaMeta += "  <form id=\"data-form\" class=\"form-card\" action=\"" + action + "\" method=\"post\" novalidate>";

            umaMeta += "    <input type=\"hidden\" name=\"id_user\" value=\"1\">";


            umaMeta += "    <div class=\"form-group\">";
            umaMeta += "      <label>Nome da Meta</label>";
            umaMeta += "      <input type=\"text\" name=\"nome\" value=\""
                     + escHtml(meta.getNome()) + "\" required placeholder=\"Ex: Reserva de emergência\">";
            umaMeta += "      <span class=\"field-error-msg\"></span>";
            umaMeta += "    </div>";

            umaMeta += "    <div class=\"form-grid-row\">";
            umaMeta += "      <div class=\"form-group\">";
            umaMeta += "        <label>Valor Total (R$)</label>";
            umaMeta += "        <input type=\"number\" name=\"valorTotal\" step=\"any\" min=\"0.01\""
                     + " value=\"" + fmt(meta.getValorTotal()) + "\" required placeholder=\"0,00\">";
            umaMeta += "        <span class=\"field-error-msg\"></span>";
            umaMeta += "      </div>";
            umaMeta += "      <div class=\"form-group\">";
            umaMeta += "        <label>Valor Atual (R$)</label>";
            umaMeta += "        <input type=\"number\" name=\"valorAlcancado\" step=\"any\" min=\"0\""
                     + " value=\"" + fmt(meta.getValorAlcancado()) + "\" required placeholder=\"0,00\">";
            umaMeta += "        <span class=\"field-error-msg\"></span>";
            umaMeta += "      </div>";
            umaMeta += "    </div>";

            umaMeta += "    <div class=\"form-group\">";
            umaMeta += "      <label>Prazo <small style=\"color:#6B7280;font-weight:400\">"
                     + "(deve ser uma data futura)</small></label>";
            umaMeta += "      <input type=\"date\" name=\"dataPrazo\""
                     + " value=\"" + meta.getDataPrazo() + "\""
                     + " min=\"" + LocalDate.now().plusDays(1) + "\" required>";
            umaMeta += "      <span class=\"field-error-msg\"></span>";
            umaMeta += "    </div>";


            umaMeta += "    <div class=\"form-group\">";
            umaMeta += "      <label>Descrição</label>";
            umaMeta += "      <textarea name=\"descricao\" placeholder=\"Descreva sua meta...\">"
                     + escHtml(meta.getDescricao()) + "</textarea>";
            umaMeta += "    </div>";

            umaMeta += "    <p style=\"font-size:0.8rem;color:#6B7280;margin:0 0 1rem;\">"
                     + "<i class=\"fa-solid fa-circle-info\" style=\"color:#2563EB\"></i> "
                     + "A meta será marcada como <strong style=\"color:#10b981\">concluída</strong> "
                     + "automaticamente quando o Valor Atual atingir o Valor Total.</p>";

            umaMeta += "    <button type=\"submit\" class=\"btn btn-primary btn-block\">"
                     + buttonLabel + "</button>";
            umaMeta += "  </form>";
            umaMeta += "</div><br><br>";
        }

        form = form.replace("<UMA-META>", umaMeta);

        String list = "<div id=\"page-list\" class=\"page active\">";
        list += "<div class=\"header-list\"><h3>Suas Metas &nbsp;"
              + "<small style=\"font-size:0.8rem;color:#6B7280;font-weight:400\">Ordenar por: "
              + "<a href=\"/metas/list/" + FORM_ORDERBY_ID    + "\">ID</a> | "
              + "<a href=\"/metas/list/" + FORM_ORDERBY_NOME  + "\">Nome</a> | "
              + "<a href=\"/metas/list/" + FORM_ORDERBY_VALOR + "\">Valor</a>"
              + "</small></h3></div>";

        list += "<div id=\"list-container\">";

        List<Metas> metas;
        if      (orderBy == FORM_ORDERBY_ID)    metas = metasDao.getOrderById();
        else if (orderBy == FORM_ORDERBY_NOME)  metas = metasDao.getOrderByNome();
        else if (orderBy == FORM_ORDERBY_VALOR) metas = metasDao.getOrderByValorTotal();
        else                                    metas = metasDao.get();

        for (Metas m : metas) {
            boolean isRealmenteConcluida = m.getValorAlcancado() >= m.getValorTotal();
            String cssStatus = isRealmenteConcluida ? "status-concluida" : "";
            
            double progresso = (m.getValorTotal() > 0)
                             ? (m.getValorAlcancado() / m.getValorTotal()) * 100 : 0;
            
            if (progresso > 100) progresso = 100;

            list += "<div class=\"list-item " + cssStatus + "\">";

            list += "  <div class=\"item-header\" style=\"display:flex;align-items:center;gap:10px;flex-wrap:wrap;\">";
            list += "    <h3 style=\"margin:0;flex:1\">" + escHtml(m.getNome()) + "</h3>";
            
            if (isRealmenteConcluida) {
                list += "    <span class=\"badge-concluida\">"
                      + "<i class=\"fa-solid fa-check\"></i> Concluída</span>";
            }
            list += "  </div>";

            list += "  <div class=\"item-details\">";
            list += "    <div>Valor Total<span>R$ " + fmt(m.getValorTotal()) + "</span></div>";
            list += "    <div>Alcançado<span>R$ "  + fmt(m.getValorAlcancado()) + "</span></div>";
            list += "    <div>Prazo<span>"         + m.getDataPrazo() + "</span></div>";
            list += "  </div>";


            list += "  <div class=\"item-progress\">";
            list += "    <progress value=\"" + m.getValorAlcancado()
                  + "\" max=\"" + m.getValorTotal() + "\"></progress>";
            list += "    <span class=\"progress-text\">" + String.format("%.1f", progresso) + "%</span>";
            list += "  </div>";


            list += "  <div class=\"item-actions\">";
            if (!isRealmenteConcluida) {
                list += "    <button class=\"btn-aporte\""
                      + " data-id=\""       + m.getId_meta()                     + "\""
                      + " data-nome=\""     + escAttr(m.getNome())               + "\""
                      + " data-total=\""    + m.getValorTotal()                   + "\""
                      + " data-alcancado=\""+ m.getValorAlcancado()               + "\">"
                      + "<i class=\"fa-solid fa-circle-plus\"></i> Aporte</button>";
            }
            list += "    <a href=\"/metas/update/" + m.getId_meta()
                  + "\" class=\"btn-icon\" style=\"color:#3b82f6;\">"
                  + "<i class=\"fa-solid fa-pen-to-square\"></i> Editar</a>";
            list += "    <a href=\"/metas/delete/" + m.getId_meta()
                  + "\" class=\"btn-icon\" style=\"color:#ef4444;\""
                  + " onclick=\"return confirm('Excluir meta: " + escAttr(m.getNome()) + "?')\">"
                  + "<i class=\"fa-solid fa-trash\"></i> Excluir</a>";
            list += "  </div>";
            list += "</div>";
        }

        if (metas.isEmpty()) {
            list += "<div style=\"text-align:center;padding:3rem;color:#6B7280;\">"
                  + "<i class=\"fa-solid fa-bullseye\" style=\"font-size:2rem;margin-bottom:1rem;display:block;\"></i>"
                  + "Nenhuma meta cadastrada ainda. Crie sua primeira meta acima!</div>";
        }

        list += "</div></div>";
        form = form.replace("<LISTAR-METAS>", list);
    }


    private String escAttr(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("\"","&quot;").replace("'","&#39;")
                .replace("<","&lt;").replace(">","&gt;");
    }


    private String escHtml(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    private String injectMsg(String html, String msg) {
        return html.replaceFirst(
            "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
            "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + escAttr(msg) + "\">"
        );
    }


    public Object insert(Request request, Response response) {
        String nome             = request.queryParams("nome");
        String valorTotalStr    = request.queryParams("valorTotal");
        String valorAlcancadoStr= request.queryParams("valorAlcancado");
        String dataPrazoStr     = request.queryParams("dataPrazo");
        String descricao        = request.queryParams("descricao");
        String id_userStr       = request.queryParams("id_user");


        String erro = validarCampos(nome, valorTotalStr, valorAlcancadoStr, dataPrazoStr, true);
        if (!erro.isEmpty()) {
            response.status(400);
            makeForm();
            return injectMsg(form, erro);
        }

        int    id_user       = Integer.parseInt(id_userStr.trim());
        double valorTotal    = Double.parseDouble(valorTotalStr.replace(",", "."));
        double valorAlcancado= Double.parseDouble(valorAlcancadoStr.replace(",", "."));
        LocalDate dataPrazo  = LocalDate.parse(dataPrazoStr.trim());
        boolean status       = valorAlcancado >= valorTotal; // automático

        Metas meta = new Metas(-1, id_user, nome.trim(), valorTotal, valorAlcancado,
                                dataPrazo, descricao != null ? descricao : "", status);

        String resp;
        if (metasDao.insert(meta)) {
            resp = status
                ? "Meta cadastrada e já concluída! Parabéns!"
                : "Meta cadastrada com sucesso!";
            response.status(201);
        } else {
            resp = "Erro ao inserir meta. Tente novamente.";
            response.status(500);
        }

        makeForm();
        return injectMsg(form, resp);
    }


    public Object getToUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Metas meta = metasDao.get(id);

        if (meta != null) {
            response.status(200);
            makeForm(FORM_UPDATE, meta, FORM_ORDERBY_NOME);
        } else {
            response.status(404);
            makeForm();
        }
        return form;
    }


    public Object update(Request request, Response response) {
        int id   = Integer.parseInt(request.params(":id"));
        Metas meta = metasDao.get(id);
        String resp;

        if (meta == null) {
            response.status(404);
            makeForm();
            return injectMsg(form, "Erro: Meta não encontrada.");
        }

        String nome             = request.queryParams("nome");
        String valorTotalStr    = request.queryParams("valorTotal");
        String valorAlcancadoStr= request.queryParams("valorAlcancado");
        String dataPrazoStr     = request.queryParams("dataPrazo");
        String descricao        = request.queryParams("descricao");

        String erro = validarCampos(nome, valorTotalStr, valorAlcancadoStr, dataPrazoStr, false);
        if (!erro.isEmpty()) {
            response.status(400);
            makeForm(FORM_UPDATE, meta, FORM_ORDERBY_NOME);
            return injectMsg(form, erro);
        }

        double valorTotal    = Double.parseDouble(valorTotalStr.replace(",", "."));
        double valorAlcancado= Double.parseDouble(valorAlcancadoStr.replace(",", "."));

        meta.setNome(nome.trim());
        meta.setValorTotal(valorTotal);
        meta.setValorAlcancado(valorAlcancado);
        meta.setDataPrazo(LocalDate.parse(dataPrazoStr.trim()));
        meta.setDescricao(descricao != null ? descricao : "");
        meta.setStatus(valorAlcancado >= valorTotal); 

        if (metasDao.update(meta)) {
            resp = meta.isStatus()
                ? "🎉 Meta concluída automaticamente! Parabéns!"
                : "Meta atualizada com sucesso!";
            response.status(200);
        } else {
            resp = "Erro ao atualizar a meta.";
            response.status(500);
        }

        makeForm();
        return injectMsg(form, resp);
    }

  
    public Object getAll(Request request, Response response) {
        int orderBy = Integer.parseInt(request.params(":orderby"));
        makeForm(orderBy);
        response.header("Content-Type", "text/html; charset=UTF-8");
        return form;
    }


    public Object delete(Request request, Response response) {
        int id     = Integer.parseInt(request.params(":id"));
        Metas meta = metasDao.get(id);
        String resp;

        if (meta != null) {
            metasDao.delete(id);
            resp = "Meta \"" + meta.getNome() + "\" excluída com sucesso.";
            response.status(200);
        } else {
            resp = "Erro: Meta não encontrada.";
            response.status(404);
        }

        makeForm();
        return injectMsg(form, resp);
    }


    public Object aporte(Request request, Response response) {
        response.type("application/json");

        int id = -1;
        try {
            id = Integer.parseInt(request.params(":id"));
        } catch (NumberFormatException e) {
            response.status(400);
            return json(false, false, "Erro: ID de meta inválido.");
        }

        String valorStr = request.queryParams("valor");


        if (valorStr == null || valorStr.trim().isEmpty()) {
            response.status(400);
            return json(false, false, "Erro: Informe o valor do aporte.");
        }

        double valorAporte;
        try {
            valorAporte = Double.parseDouble(valorStr.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            response.status(400);
            return json(false, false, "Erro: Valor inválido. Digite apenas números (ex: 150.00).");
        }

        if (valorAporte <= 0) {
            response.status(400);
            return json(false, false, "Erro: O valor do aporte deve ser maior que zero.");
        }
        if (Double.isInfinite(valorAporte) || Double.isNaN(valorAporte)) {
            response.status(400);
            return json(false, false, "Erro: Valor inválido.");
        }


        Metas meta = metasDao.get(id);
        if (meta == null) {
            response.status(404);
            return json(false, false, "Erro: Meta não encontrada.");
        }
        if (meta.isStatus()) {
            response.status(400);
            return json(false, false, "Erro: Esta meta já está concluída.");
        }

        double disponivel = Math.round((meta.getValorTotal() - meta.getValorAlcancado()) * 100.0) / 100.0;
        if (valorAporte > disponivel + 0.001) { // tolerância de arredondamento
            response.status(400);
            return json(false, false,
                "Erro: O aporte de R$ " + fmt(valorAporte) +
                " excede o valor disponível (R$ " + fmt(disponivel) + ").");
        }

        double novoValor = Math.round((meta.getValorAlcancado() + valorAporte) * 100.0) / 100.0;

        if (novoValor > meta.getValorTotal()) novoValor = meta.getValorTotal();

        meta.setValorAlcancado(novoValor);
        meta.setStatus(novoValor >= meta.getValorTotal()); // automático

        if (!metasDao.update(meta)) {
            response.status(500);
            return json(false, false, "Erro interno ao registrar o aporte. Tente novamente.");
        }

        response.status(200);
        if (meta.isStatus()) {
            return json(true, true,
                "🎉 Parabéns! Meta \"" + meta.getNome() + "\" concluída com sucesso!");
        }
        return json(true, false,
            "Aporte de R$ " + fmt(valorAporte) + " realizado com sucesso em \"" + meta.getNome() + "\"!");
    }

    
    private String json(boolean success, boolean concluida, String message) {
        return String.format("{\"success\":%b,\"concluida\":%b,\"message\":\"%s\"}",
            success, concluida, message.replace("\"", "\\\"").replace("\n", ""));
    }
}
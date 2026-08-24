package service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import DAO.AtivoFinanceiroDao;
import DAO.ItemSimulacaoDao;
import DAO.SimulacaoCarteiraDao;
import model.ItemSimulacao;
import model.SimulacaoCarteira;
import spark.Request;
import spark.Response;

public class SimulacaoService {

    private final SimulacaoCarteiraDao carteiraDao = new SimulacaoCarteiraDao();
    private final ItemSimulacaoDao     itemDao     = new ItemSimulacaoDao();
    private final AtivoFinanceiroDao   ativoDao    = new AtivoFinanceiroDao();

    private static final int ID_USUARIO = 1;
    private static final String HTML_FILE = "src/main/resources/public/simulation.html";

    private String form = "";

    public void makeForm() { makeForm(""); }

    public void makeForm(String mensagem) {
        form = "";
        try {
            Scanner sc = new Scanner(new File(HTML_FILE));
            while (sc.hasNext()) form += sc.nextLine() + "\n";
            sc.close();
        } catch (Exception e) {
            System.err.println("SimulacaoService: erro ao ler HTML – " + e.getMessage());
        }
        SimulacaoCarteira carteira = carteiraDao.getOrCreate(ID_USUARIO);
        List<ItemSimulacao> itens  = itemDao.getBySimulacao(carteira.getId_simulacao());
        form = form.replace("<CARTEIRA-SIMULADA>", buildCarteiraHtml(carteira, itens, mensagem));
    }

    public Object index(Request req, Response res) {
        makeForm();
        res.header("Content-Type", "text/html; charset=UTF-8");
        return form;
    }

    public Object depositar(Request req, Response res) {
        String resp;
        try {
            BigDecimal valor = new BigDecimal(req.queryParams("valor").replace(",", "."));
            if (valor.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Valor inválido");
            SimulacaoCarteira c = carteiraDao.getOrCreate(ID_USUARIO);
            carteiraDao.updateSaldo(c.getId_simulacao(), c.getSaldo().add(valor));
            resp = "ok|Depósito de R$ " + String.format("%.2f", valor) + " realizado com sucesso!";
        } catch (Exception e) {
            resp = "erro|Erro no depósito: " + e.getMessage();
        }
        makeForm(resp);
        res.header("Content-Type", "text/html; charset=UTF-8");
        return form;
    }

    public Object comprar(Request req, Response res) {
        String resp;
        try {
            String     ticket    = req.queryParams("ticket").toUpperCase().trim();
            String     nomeAtivo = req.queryParams("nome_ativo");
            String     setor     = req.queryParams("setor");
            String     tipo      = req.queryParams("tipo");
            int        qtd       = Integer.parseInt(req.queryParams("quantidade"));
            BigDecimal preco     = new BigDecimal(req.queryParams("preco_compra").replace(",", "."));
            BigDecimal custo     = preco.multiply(BigDecimal.valueOf(qtd));

            SimulacaoCarteira c = carteiraDao.getOrCreate(ID_USUARIO);
            if (c.getSaldo().compareTo(custo) < 0) {
                resp = "erro|Saldo insuficiente! Saldo: R$ " + String.format("%.2f", c.getSaldo())
                     + " | Custo total: R$ " + String.format("%.2f", custo);
            } else {
                int idAtivo = ativoDao.getOrCreateId(ticket, nomeAtivo, setor, tipo);
                ItemSimulacao item = new ItemSimulacao();
                item.setId_simulacao(c.getId_simulacao());
                item.setId_ativo(idAtivo);
                item.setQuantidade(qtd);
                item.setPreco_compra(preco);
                itemDao.insert(item);
                carteiraDao.updateSaldo(c.getId_simulacao(), c.getSaldo().subtract(custo));
                resp = "ok|Compra de " + qtd + "x " + ticket + " por R$ "
                     + String.format("%.4f", preco) + " realizada! Custo total: R$ "
                     + String.format("%.2f", custo);
            }
        } catch (Exception e) {
            resp = "erro|Erro na compra: " + e.getMessage();
        }
        makeForm(resp);
        res.header("Content-Type", "text/html; charset=UTF-8");
        return form;
    }

    public Object vender(Request req, Response res) {
        String resp;
        try {
            int idItem = Integer.parseInt(req.params(":id_item"));
            ItemSimulacao item = itemDao.get(idItem);
            if (item == null) {
                resp = "erro|Item não encontrado.";
            } else {
                BigDecimal devolvido = item.getValorTotal();
                itemDao.delete(idItem);
                SimulacaoCarteira c = carteiraDao.getOrCreate(ID_USUARIO);
                carteiraDao.updateSaldo(c.getId_simulacao(), c.getSaldo().add(devolvido));
                resp = "ok|Posição " + item.getTicket() + " vendida! R$ "
                     + String.format("%.2f", devolvido) + " devolvido ao saldo.";
            }
        } catch (Exception e) {
            resp = "erro|Erro ao vender: " + e.getMessage();
        }
        makeForm(resp);
        res.header("Content-Type", "text/html; charset=UTF-8");
        return form;
    }

    private String buildCarteiraHtml(SimulacaoCarteira carteira,
                                     List<ItemSimulacao> itens,
                                     String mensagem) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"sim-carteira-section\">");

        sb.append("<div class=\"sim-carteira-header\">");
        sb.append("  <div class=\"sim-carteira-title\">");
        sb.append("    <i class=\"fa-solid fa-wallet\"></i>");
        sb.append("    <h3>Minha Carteira Simulada</h3>");
        sb.append("  </div>");
        sb.append("  <div class=\"sim-saldo-badge\">");
        sb.append("    <span class=\"sim-saldo-label\">Saldo disponível</span>");
        sb.append("    <span class=\"sim-saldo-valor\">R$ ").append(String.format("%.2f", carteira.getSaldo())).append("</span>");
        sb.append("  </div>");
        sb.append("</div>");

        if (mensagem != null && !mensagem.isEmpty()) {
            String[] partes = mensagem.split("\\|", 2);
            String tipo  = partes.length == 2 ? partes[0] : "ok";
            String texto = partes.length == 2 ? partes[1] : mensagem;
            String cls   = tipo.equals("erro") ? "sim-msg sim-msg-erro" : "sim-msg sim-msg-ok";
            String icone = tipo.equals("erro") ? "circle-xmark" : "circle-check";
            sb.append("<div class=\"").append(cls).append("\">")
              .append("<i class=\"fa-solid fa-").append(icone).append("\"></i> ")
              .append(texto).append("</div>");
        }

        sb.append("<div class=\"sim-acoes-grid\">");

        sb.append("<div class=\"sim-form-card\">");
        sb.append("  <h4><i class=\"fa-solid fa-coins\"></i> Depositar Saldo</h4>");
        sb.append("  <form action=\"/simulation/depositar\" method=\"post\" class=\"sim-inline-form\">");
        sb.append("    <div class=\"sim-form-group\">");
        sb.append("      <label>Valor (R$)</label>");
        sb.append("      <input type=\"number\" name=\"valor\" step=\"0.01\" min=\"0.01\" placeholder=\"0,00\" required>");
        sb.append("    </div>");
        sb.append("    <button type=\"submit\" class=\"sim-btn sim-btn-success\"><i class=\"fa-solid fa-plus\"></i> Depositar</button>");
        sb.append("  </form>");
        sb.append("</div>");

        sb.append("<div class=\"sim-form-card\">");
        sb.append("  <h4><i class=\"fa-solid fa-arrow-trend-up\"></i> Comprar Ativo</h4>");
        sb.append("  <form action=\"/simulation/comprar\" method=\"post\" class=\"sim-comprar-form\">");

        sb.append("    <div class=\"sim-form-row\">");
        sb.append("      <div class=\"sim-form-group\">");
        sb.append("        <label>Ticker</label>");
        sb.append("        <input type=\"text\" id=\"inputTickerCompra\" name=\"ticket\" placeholder=\"Ex: PETR4\" maxlength=\"10\" required readonly>");
        sb.append("        <small class=\"sim-hint\">← Selecione um ativo na lista acima</small>");
        sb.append("      </div>");
        sb.append("      <div class=\"sim-form-group\">");
        sb.append("        <label>Nome do Ativo</label>");
        sb.append("        <input type=\"text\" id=\"inputNomeCompra\" name=\"nome_ativo\" placeholder=\"—\" required readonly>");
        sb.append("      </div>");
        sb.append("    </div>");

        sb.append("    <div class=\"sim-form-row\">");
        sb.append("      <div class=\"sim-form-group\">");
        sb.append("        <label>Tipo</label>");
        sb.append("        <select name=\"tipo\" id=\"inputTipoCompra\">");
        sb.append("          <option value=\"stock\">Ação</option>");
        sb.append("          <option value=\"fund\">Fundo</option>");
        sb.append("          <option value=\"bdr\">BDR</option>");
        sb.append("        </select>");
        sb.append("      </div>");
        sb.append("      <div class=\"sim-form-group\">");
        sb.append("        <label>Setor</label>");
        sb.append("        <select name=\"setor\" id=\"inputSetorCompra\">");
        sb.append("          <option value=\"Finance\">Financeiro</option>");
        sb.append("          <option value=\"Energy Minerals\">Energia e Minerais</option>");
        sb.append("          <option value=\"Technology Services\">Tecnologia</option>");
        sb.append("          <option value=\"Health Services\">Saúde</option>");
        sb.append("          <option value=\"Consumer Non-Durables\">Consumo</option>");
        sb.append("          <option value=\"Retail Trade\">Varejo</option>");
        sb.append("          <option value=\"Utilities\">Serviços Públicos</option>");
        sb.append("          <option value=\"Transportation\">Transporte</option>");
        sb.append("          <option value=\"Communications\">Comunicações</option>");
        sb.append("          <option value=\"Miscellaneous\">Outros</option>");
        sb.append("        </select>");
        sb.append("      </div>");
        sb.append("    </div>");

        sb.append("    <div class=\"sim-form-row\">");
        sb.append("      <div class=\"sim-form-group\">");
        sb.append("        <label>Quantidade</label>");
        sb.append("        <input type=\"number\" id=\"inputQtdCompra\" name=\"quantidade\" min=\"1\" placeholder=\"1\" required>");
        sb.append("      </div>");
        sb.append("      <div class=\"sim-form-group\">");
        sb.append("        <label>Preço de Compra (R$) — cotação atual</label>");
        sb.append("        <input type=\"number\" id=\"inputPrecoCompra\" name=\"preco_compra\" step=\"0.0001\" min=\"0.01\" placeholder=\"Aguardando seleção...\" required readonly>");
        sb.append("      </div>");
        sb.append("    </div>");

        sb.append("    <div id=\"sim-cotacao-badge\" class=\"sim-cotacao-badge\" style=\"display:none\">");
        sb.append("      <div class=\"sim-cotacao-info\">");
        sb.append("        <i class=\"fa-solid fa-chart-line\"></i>");
        sb.append("        Cotação: <strong id=\"sim-cotacao-valor\">—</strong>");
        sb.append("        <span id=\"sim-cotacao-variacao\" class=\"sim-cotacao-var\"></span>");
        sb.append("      </div>");
        sb.append("      <div id=\"sim-custo-total\" class=\"sim-custo-total\"></div>");
        sb.append("    </div>");

        sb.append("    <button type=\"submit\" id=\"btnComprar\" class=\"sim-btn sim-btn-primary\" disabled>");
        sb.append("      <i class=\"fa-solid fa-cart-shopping\"></i> Comprar");
        sb.append("    </button>");
        sb.append("  </form>");
        sb.append("</div>"); 

        sb.append("</div>"); 

        sb.append("<div class=\"sim-posicoes\">");
        sb.append("<div class=\"sim-posicoes-titulo\">");
        sb.append("  <h4><i class=\"fa-solid fa-table-list\"></i> Posições na Carteira</h4>");

        if (!itens.isEmpty()) {
            BigDecimal totalCompra = itens.stream()
                    .map(ItemSimulacao::getValorTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            sb.append("  <div class=\"sim-resumo-bar\">");
            sb.append("    <div><span class=\"sim-resumo-label\">Investido</span>");
            sb.append("         <span class=\"sim-resumo-val\">R$ ").append(String.format("%.2f", totalCompra)).append("</span></div>");
            sb.append("    <div><span class=\"sim-resumo-label\">Valor Atual</span>");
            sb.append("         <span class=\"sim-resumo-val\" id=\"sim-total-atual\">carregando...</span></div>");
            sb.append("    <div><span class=\"sim-resumo-label\">Resultado</span>");
            sb.append("         <span class=\"sim-resumo-val\" id=\"sim-total-resultado\">—</span></div>");
            sb.append("    <div><span class=\"sim-resumo-label\">Variação</span>");
            sb.append("         <span class=\"sim-resumo-val\" id=\"sim-total-variacao\">—</span></div>");
            sb.append("  </div>");
        }

        sb.append("</div>"); 

        if (itens.isEmpty()) {
            sb.append("<div class=\"sim-empty\">");
            sb.append("  <i class=\"fa-solid fa-folder-open\" style=\"font-size:2rem;color:var(--text-gray)\"></i>");
            sb.append("  <p>Nenhum ativo na carteira ainda.<br>Deposite saldo e faça sua primeira compra!</p>");
            sb.append("</div>");
        } else {
            sb.append("<div class=\"sim-posicoes-list\">");
            for (ItemSimulacao item : itens) {
                String ticket = item.getTicket()    != null ? item.getTicket()    : "";
                String nome   = item.getNome_ativo() != null ? item.getNome_ativo() : "—";
                String tipo   = item.getTipo()      != null ? item.getTipo()      : "";

                sb.append("<div class=\"sim-posicao-card\"")
                  .append(" data-ticket=\"").append(ticket).append("\"")
                  .append(" data-preco-compra=\"").append(item.getPreco_compra()).append("\"")
                  .append(" data-quantidade=\"").append(item.getQuantidade()).append("\"")
                  .append(">");

                sb.append("  <div class=\"sim-pos-header\">");
                sb.append("    <div>");
                sb.append("      <div class=\"sim-pos-ticket\">").append(ticket).append("</div>");
                sb.append("      <div class=\"sim-pos-nome\">").append(nome).append("</div>");
                sb.append("    </div>");
                sb.append("    <span class=\"sim-pos-tipo sim-pos-tipo-").append(tipo).append("\">")
                  .append(tipo.isEmpty() ? "—" : tipo.toUpperCase()).append("</span>");
                sb.append("  </div>");

                sb.append("  <div class=\"sim-pos-compra\">");
                sb.append("    <div class=\"sim-pos-compra-item\"><span>Qtd.</span><strong>").append(item.getQuantidade()).append("</strong></div>");
                sb.append("    <div class=\"sim-pos-compra-item\"><span>Preço compra</span><strong>R$ ")
                  .append(String.format("%.4f", item.getPreco_compra())).append("</strong></div>");
                sb.append("    <div class=\"sim-pos-compra-item\"><span>Total compra</span><strong>R$ ")
                  .append(String.format("%.2f", item.getValorTotal())).append("</strong></div>");
                sb.append("    <div class=\"sim-pos-compra-item\"><span>Data</span><strong>")
                  .append(item.getData_compra()).append("</strong></div>");
                sb.append("  </div>");

                sb.append("  <div class=\"sim-pos-mercado\">");
                sb.append("    <div class=\"sim-pos-mercado-item loading\"><span>Cotação atual</span><strong class=\"sim-preco-atual\"><i class=\"fa-solid fa-spinner fa-spin\"></i></strong></div>");
                sb.append("    <div class=\"sim-pos-mercado-item loading\"><span>Valor atual</span><strong class=\"sim-valor-atual\"><i class=\"fa-solid fa-spinner fa-spin\"></i></strong></div>");
                sb.append("    <div class=\"sim-pos-mercado-item loading\"><span>Resultado</span><strong class=\"sim-resultado\"><i class=\"fa-solid fa-spinner fa-spin\"></i></strong></div>");
                sb.append("    <div class=\"sim-pos-mercado-item loading\"><span>Variação %</span><strong class=\"sim-variacao\"><i class=\"fa-solid fa-spinner fa-spin\"></i></strong></div>");
                sb.append("  </div>");

                sb.append("  <a href=\"/simulation/vender/").append(item.getId_item())
                  .append("\" class=\"sim-btn-vender\"")
                  .append(" onclick=\"return confirm('Vender ").append(item.getQuantidade())
                  .append("x ").append(ticket).append("? O valor retornará ao saldo.')\">");
                sb.append("    <i class=\"fa-solid fa-right-from-bracket\"></i> Vender");
                sb.append("  </a>");

                sb.append("</div>"); 
            }
            sb.append("</div>"); 
        }

        sb.append("</div>"); 
        sb.append("</div>"); 
        return sb.toString();
    }
}
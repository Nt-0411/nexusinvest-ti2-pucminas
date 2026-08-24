package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ItemSimulacao {

    private int id_item;
    private int quantidade;
    private LocalDate data_compra;
    private BigDecimal preco_compra;
    private int id_simulacao;
    private int id_ativo;

    // Campos extras para exibição (join com ativo_financeiro)
    private String ticket;
    private String nome_ativo;
    private String tipo;

    public ItemSimulacao() {
        this.id_item      = -1;
        this.quantidade   = 0;
        this.data_compra  = LocalDate.now();
        this.preco_compra = BigDecimal.ZERO;
        this.id_simulacao = -1;
        this.id_ativo     = -1;
        this.ticket       = "";
        this.nome_ativo   = "";
        this.tipo         = "";
    }

    public ItemSimulacao(int id_item, int quantidade, LocalDate data_compra,
                         BigDecimal preco_compra, int id_simulacao, int id_ativo) {
        this.id_item      = id_item;
        this.quantidade   = quantidade;
        this.data_compra  = data_compra;
        this.preco_compra = preco_compra;
        this.id_simulacao = id_simulacao;
        this.id_ativo     = id_ativo;
    }

    /** Valor total simulado = quantidade × preço de compra */
    public BigDecimal getValorTotal() {
        return preco_compra.multiply(BigDecimal.valueOf(quantidade));
    }

    public int getId_item()                   { return id_item; }
    public void setId_item(int id_item)       { this.id_item = id_item; }

    public int getQuantidade()                     { return quantidade; }
    public void setQuantidade(int quantidade)      { this.quantidade = quantidade; }

    public LocalDate getData_compra()                    { return data_compra; }
    public void setData_compra(LocalDate data_compra)    { this.data_compra = data_compra; }

    public BigDecimal getPreco_compra()                        { return preco_compra; }
    public void setPreco_compra(BigDecimal preco_compra)       { this.preco_compra = preco_compra; }

    public int getId_simulacao()                    { return id_simulacao; }
    public void setId_simulacao(int id_simulacao)   { this.id_simulacao = id_simulacao; }

    public int getId_ativo()                  { return id_ativo; }
    public void setId_ativo(int id_ativo)     { this.id_ativo = id_ativo; }

    public String getTicket()               { return ticket; }
    public void setTicket(String ticket)    { this.ticket = ticket; }

    public String getNome_ativo()                { return nome_ativo; }
    public void setNome_ativo(String nome_ativo) { this.nome_ativo = nome_ativo; }

    public String getTipo()              { return tipo; }
    public void setTipo(String tipo)     { this.tipo = tipo; }

    @Override
    public String toString() {
        return "ItemSimulacao [id_item=" + id_item +
               ", ticket=" + ticket +
               ", quantidade=" + quantidade +
               ", preco_compra=" + preco_compra +
               ", data_compra=" + data_compra +
               ", id_simulacao=" + id_simulacao +
               ", id_ativo=" + id_ativo + "]";
    }
}

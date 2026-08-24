package model;

public class AtivoFinanceiro {

    private int id_ativo;
    private String ticket;
    private String nome_ativo;
    private String setor;
    private String tipo;

    public AtivoFinanceiro() {
        this.id_ativo = -1;
        this.ticket   = "";
        this.nome_ativo = "";
        this.setor    = "";
        this.tipo     = "";
    }

    public AtivoFinanceiro(int id_ativo, String ticket, String nome_ativo, String setor, String tipo) {
        this.id_ativo   = id_ativo;
        this.ticket     = ticket;
        this.nome_ativo = nome_ativo;
        this.setor      = setor;
        this.tipo       = tipo;
    }

    public int getId_ativo()             { return id_ativo; }
    public void setId_ativo(int id_ativo){ this.id_ativo = id_ativo; }

    public String getTicket()               { return ticket; }
    public void setTicket(String ticket)    { this.ticket = ticket; }

    public String getNome_ativo()                { return nome_ativo; }
    public void setNome_ativo(String nome_ativo) { this.nome_ativo = nome_ativo; }

    public String getSetor()             { return setor; }
    public void setSetor(String setor)   { this.setor = setor; }

    public String getTipo()              { return tipo; }
    public void setTipo(String tipo)     { this.tipo = tipo; }

    @Override
    public String toString() {
        return "AtivoFinanceiro [id_ativo=" + id_ativo +
               ", ticket=" + ticket +
               ", nome_ativo=" + nome_ativo +
               ", setor=" + setor +
               ", tipo=" + tipo + "]";
    }
}

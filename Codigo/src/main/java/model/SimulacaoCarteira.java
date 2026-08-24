package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SimulacaoCarteira {

    private int id_simulacao;
    private BigDecimal saldo;
    private LocalDateTime data_simulacao;
    private int id_usuario;

    public SimulacaoCarteira() {
        this.id_simulacao  = -1;
        this.saldo         = BigDecimal.ZERO;
        this.data_simulacao = LocalDateTime.now();
        this.id_usuario    = -1;
    }

    public SimulacaoCarteira(int id_simulacao, BigDecimal saldo,
                             LocalDateTime data_simulacao, int id_usuario) {
        this.id_simulacao   = id_simulacao;
        this.saldo          = saldo;
        this.data_simulacao = data_simulacao;
        this.id_usuario     = id_usuario;
    }

    public int getId_simulacao()                    { return id_simulacao; }
    public void setId_simulacao(int id_simulacao)   { this.id_simulacao = id_simulacao; }

    public BigDecimal getSaldo()                    { return saldo; }
    public void setSaldo(BigDecimal saldo)          { this.saldo = saldo; }

    public LocalDateTime getData_simulacao()                       { return data_simulacao; }
    public void setData_simulacao(LocalDateTime data_simulacao)    { this.data_simulacao = data_simulacao; }

    public int getId_usuario()                      { return id_usuario; }
    public void setId_usuario(int id_usuario)       { this.id_usuario = id_usuario; }

    @Override
    public String toString() {
        return "SimulacaoCarteira [id_simulacao=" + id_simulacao +
               ", saldo=" + saldo +
               ", data_simulacao=" + data_simulacao +
               ", id_usuario=" + id_usuario + "]";
    }
}

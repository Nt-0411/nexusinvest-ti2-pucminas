package model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Metas {

	private int id_meta;
	private int id_user;	
	private String nome;
	private double valorTotal;
	private double valorAlcancado;
	private String descricao;
	private boolean status;
	private LocalDate dataPrazo;
	private OffsetDateTime dataCriacao;
	private OffsetDateTime dataAtualizado;

	public Metas() {
		this.id_meta = -1;
		this.id_user = -1;
		this.nome = "";
		this.valorTotal = 0.0;
		this.valorAlcancado = 0.0;
		this.descricao = "";
		this.status = false;
		this.dataPrazo = LocalDate.now().plusMonths(6); 
		this.dataCriacao = OffsetDateTime.now();
		this.dataAtualizado = OffsetDateTime.now();
	}

	public Metas (int id_meta, int id_user, String nome, double valorTotal, double valorAlcancado,
			LocalDate dataPrazo, String descricao, Boolean status) {

		this.id_meta = id_meta;
		this.id_user = id_user;
		this.nome = nome;
		this.valorTotal = valorTotal;
		this.valorAlcancado = valorAlcancado;
		this.dataPrazo = dataPrazo;
		this.descricao = descricao;
		this.status = status;
	}
	
	public int getId_meta() {
		return id_meta;
	}

	public void setId_meta(int id_meta) {
		this.id_meta = id_meta;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public double getValorAlcancado() {
		return valorAlcancado;
	}

	public void setValorAlcancado(double valorAlcancado) {
		this.valorAlcancado = valorAlcancado;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public LocalDate getDataPrazo() {
		return dataPrazo;
	}

	public void setDataPrazo(LocalDate dataPrazo) {
		this.dataPrazo = dataPrazo;
	}

	public OffsetDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(OffsetDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public OffsetDateTime getDataAtualizado() {
		return dataAtualizado;
	}

	public void setDataAtualizado(OffsetDateTime dataAtualizado) {
		this.dataAtualizado = dataAtualizado;
	}

	@Override
	public String toString() {
		return "Metas [id_meta=" + id_meta + 
				", id_user=" + id_user + 
				", nome=" + nome + 
				", valorTotal=" + valorTotal + 
				", valorAlcancado=" + valorAlcancado + 
				", descricao=" + descricao + 
				", status=" + status + 
				", dataPrazo=" + dataPrazo + 
				", dataCriacao=" + dataCriacao + 
				", dataAtualizado=" + dataAtualizado + "]";
	}
}
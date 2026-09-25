package br.com.araujoabreu.sgi.service.feriado.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class FeriadoTO extends BaseTO {

	private Integer ano;
	private String descricao;
	private Integer dia;
	private Integer mes;

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}
}

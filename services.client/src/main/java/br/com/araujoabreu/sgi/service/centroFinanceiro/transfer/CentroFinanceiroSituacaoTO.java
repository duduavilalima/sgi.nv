package br.com.araujoabreu.sgi.service.centroFinanceiro.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;

public class CentroFinanceiroSituacaoTO extends BaseTO {

	private CentroFinanceiroTO centroFinanceiro;
	private ColaboradorTO colaborador;
	private Date data;
	private String situacao;

	public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public ColaboradorTO getColaborador() {
		return colaborador;
	}

	public void setColaborador(ColaboradorTO colaborador) {
		this.colaborador = colaborador;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
}

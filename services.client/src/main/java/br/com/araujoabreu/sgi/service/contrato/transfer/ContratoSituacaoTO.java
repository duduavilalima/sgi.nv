package br.com.araujoabreu.sgi.service.contrato.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;

public class ContratoSituacaoTO extends BaseTO {

	private ColaboradorTO colaborador;
	private String situacao;
	private Date dataSituacao;

	public ColaboradorTO getColaborador() {
		return colaborador;
	}

	public void setColaborador(ColaboradorTO colaborador) {
		this.colaborador = colaborador;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Date getDataSituacao() {
		return dataSituacao;
	}

	public void setDataSituacao(Date dataSituacao) {
		this.dataSituacao = dataSituacao;
	}
}

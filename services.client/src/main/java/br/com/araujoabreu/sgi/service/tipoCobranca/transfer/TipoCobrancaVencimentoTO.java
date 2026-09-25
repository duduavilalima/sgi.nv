package br.com.araujoabreu.sgi.service.tipoCobranca.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class TipoCobrancaVencimentoTO extends BaseTO {

	private String dataReferencia;
	private Integer diaVencimento;
	private String diaVencimentoTipo;
	private String mesReferencia;
	private Boolean solicitaData;

	public String getDataReferencia() {
		return dataReferencia;
	}

	public void setDataReferencia(String dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	public Integer getDiaVencimento() {
		return diaVencimento;
	}

	public void setDiaVencimento(Integer diaVencimento) {
		this.diaVencimento = diaVencimento;
	}

	public String getDiaVencimentoTipo() {
		return diaVencimentoTipo;
	}

	public void setDiaVencimentoTipo(String diaVencimentoTipo) {
		this.diaVencimentoTipo = diaVencimentoTipo;
	}

	public String getMesReferencia() {
		return mesReferencia;
	}

	public void setMesReferencia(String mesReferencia) {
		this.mesReferencia = mesReferencia;
	}

	public Boolean getSolicitaData() {
		return solicitaData;
	}

	public void setSolicitaData(Boolean solicitaData) {
		this.solicitaData = solicitaData;
	}
}

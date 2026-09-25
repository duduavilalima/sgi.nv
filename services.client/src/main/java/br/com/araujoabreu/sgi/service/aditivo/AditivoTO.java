package br.com.araujoabreu.sgi.service.aditivo;

import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;

public class AditivoTO extends BaseTO {

	private Integer numero;
	private ContratoTO contrato;
	private Date dataAssinatura;
	private Date dataInicioVigencia;
	private Date dataFimVigencia;
	private Integer prazoAtual;
	private String periodoReajuste;
	private String indiceReajuste;
	private String resumo;

	public ContratoTO getContrato() {
		return contrato;
	}

	public void setContrato(ContratoTO contrato) {
		this.contrato = contrato;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Date getDataAssinatura() {
		return dataAssinatura;
	}

	public void setDataAssinatura(Date dataAssinatura) {
		this.dataAssinatura = dataAssinatura;
	}

	public Date getDataInicioVigencia() {
		return dataInicioVigencia;
	}

	public void setDataInicioVigencia(Date dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	public Date getDataFimVigencia() {
		return dataFimVigencia;
	}

	public void setDataFimVigencia(Date dataFimVigencia) {
		this.dataFimVigencia = dataFimVigencia;
	}

	public Integer getPrazoAtual() {
		return prazoAtual;
	}

	public void setPrazoAtual(Integer prazoAtual) {
		this.prazoAtual = prazoAtual;
	}

	public String getPeriodoReajuste() {
		return periodoReajuste;
	}

	public void setPeriodoReajuste(String periodoReajuste) {
		this.periodoReajuste = periodoReajuste;
	}

	public String getIndiceReajuste() {
		return indiceReajuste;
	}

	public void setIndiceReajuste(String indiceReajuste) {
		this.indiceReajuste = indiceReajuste;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public String getResumoMinimo() {
		if (resumo != null && !resumo.isEmpty()) {
			if (resumo.trim().length() <= 20) {
				return numero + " - " + resumo;
			} else {
				return numero + " - " + resumo.substring(0, 20);
			}
		}
		return "";
	}
}

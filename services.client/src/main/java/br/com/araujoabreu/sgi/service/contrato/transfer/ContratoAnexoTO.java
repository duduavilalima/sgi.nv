package br.com.araujoabreu.sgi.service.contrato.transfer;

import br.com.araujoabreu.sgi.service.aditivo.AditivoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.AnexoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class ContratoAnexoTO extends BaseTO {

	private ContratoTO contrato;
	private AditivoTO aditivo;
	private AnexoTO anexo;
	private String tipo;
	private Integer ordem;

	public ContratoTO getContrato() {
		return contrato;
	}

	public void setContrato(ContratoTO contrato) {
		this.contrato = contrato;
	}

	public AditivoTO getAditivo() {
		return aditivo;
	}

	public void setAditivo(AditivoTO aditivo) {
		this.aditivo = aditivo;
	}

	public AnexoTO getAnexo() {
		return anexo;
	}

	public void setAnexo(AnexoTO anexo) {
		this.anexo = anexo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}

package br.com.araujoabreu.sgi.service.contrato.transfer;

import br.com.araujoabreu.sgi.service.contato.transfer.ContatoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class ContratoContatoTO extends BaseTO {

	private Long codigoContrato;
	private Long codigoContato;
	private ContatoTO contato;
	private ContratoTO contrato;
	private int ordem;

	public Long getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(Long codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public Long getCodigoContato() {
		return codigoContato;
	}

	public void setCodigoContato(Long codigoContato) {
		this.codigoContato = codigoContato;
	}

	public ContatoTO getContato() {
		return contato;
	}

	public void setContato(ContatoTO contato) {
		this.contato = contato;
	}

	public ContratoTO getContrato() {
		return contrato;
	}

	public void setContrato(ContratoTO contrato) {
		this.contrato = contrato;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
}

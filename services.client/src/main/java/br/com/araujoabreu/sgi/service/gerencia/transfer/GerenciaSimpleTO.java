package br.com.araujoabreu.sgi.service.gerencia.transfer;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroSimpleTO;

public class GerenciaSimpleTO extends CentroFinanceiroSimpleTO {

	private String numero;

	public GerenciaSimpleTO() {
	}

	public GerenciaSimpleTO(Long codigo) {
		setCodigo(codigo);
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

}

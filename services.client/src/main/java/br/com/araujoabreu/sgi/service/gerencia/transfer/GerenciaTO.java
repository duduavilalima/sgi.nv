package br.com.araujoabreu.sgi.service.gerencia.transfer;

import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;

public class GerenciaTO extends CentroFinanceiroTO {

	private String numero;
	private DiretoriaSimpleTO diretoria;
	private ColaboradorTO gerente;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public DiretoriaSimpleTO getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(DiretoriaSimpleTO diretoria) {
		this.diretoria = diretoria;
	}

	public ColaboradorTO getGerente() {
		return gerente;
	}

	public void setGerente(ColaboradorTO gerente) {
		this.gerente = gerente;
	}
}

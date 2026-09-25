package br.com.araujoabreu.sgi.service.reinf.r1000;

import br.com.araujoabreu.sgi.service.reinf.IdePeriodo;
import br.com.araujoabreu.sgi.service.reinf.r1070.NovaValidade;

public class Alteracao {

	private IdePeriodo idePeriodo;
	private InfoCadastro infoCadastro;
	private NovaValidade novaValidade;

	public IdePeriodo getIdePeriodo() {
		return idePeriodo;
	}

	public void setIdePeriodo(IdePeriodo idePeriodo) {
		this.idePeriodo = idePeriodo;
	}

	public InfoCadastro getInfoCadastro() {
		return infoCadastro;
	}

	public void setInfoCadastro(InfoCadastro infoCadastro) {
		this.infoCadastro = infoCadastro;
	}

	public NovaValidade getNovaValidade() {
		return novaValidade;
	}

	public void setNovaValidade(NovaValidade novaValidade) {
		this.novaValidade = novaValidade;
	}
}

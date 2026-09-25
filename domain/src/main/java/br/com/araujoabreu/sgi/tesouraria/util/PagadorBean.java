package br.com.araujoabreu.sgi.tesouraria.util;

import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;

public class PagadorBean {

	private ContaCorrente contaCorrente;
	private PessoaJuridica pessoaJuridica;

	public PagadorBean() {
	}

	public ContaCorrente getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrente contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

}

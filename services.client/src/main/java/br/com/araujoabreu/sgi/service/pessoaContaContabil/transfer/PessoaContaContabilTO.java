package br.com.araujoabreu.sgi.service.pessoaContaContabil.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeSimpleTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class PessoaContaContabilTO extends BaseTO {

	private PessoaTO pessoa;
	private String tipoEspecial;
	private UnidadeSimpleTO unidade;
	private String numeroContaContabil;
	private ContaContabilTO contaContabil;

	public PessoaTO getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaTO pessoa) {
		this.pessoa = pessoa;
	}

	public String getTipoEspecial() {
		return tipoEspecial;
	}

	public void setTipoEspecial(String tipoEspecial) {
		this.tipoEspecial = tipoEspecial;
	}

	public UnidadeSimpleTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeSimpleTO unidade) {
		this.unidade = unidade;
	}

	public String getNumeroContaContabil() {
		return numeroContaContabil;
	}

	public void setNumeroContaContabil(String numeroContaContabil) {
		this.numeroContaContabil = numeroContaContabil;
	}

	public ContaContabilTO getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabilTO contaContabil) {
		this.contaContabil = contaContabil;
	}
}

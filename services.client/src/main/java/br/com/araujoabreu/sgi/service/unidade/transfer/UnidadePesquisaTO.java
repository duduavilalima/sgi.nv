package br.com.araujoabreu.sgi.service.unidade.transfer;


import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaPesquisaTO;

public class UnidadePesquisaTO extends PessoaJuridicaPesquisaTO {

	private String codnome;
	private String tipoUnidade;

	public String getCodnome() {
		return codnome;
	}

	public void setCodnome(String codnome) {
		this.codnome = codnome;
	}

	public String getTipoUnidade() {
		return tipoUnidade;
	}

	public void setTipoUnidade(String tipoUnidade) {
		this.tipoUnidade = tipoUnidade;
	}
}

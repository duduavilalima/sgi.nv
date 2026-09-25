package br.com.araujoabreu.sgi.service.util;

public class AutoCompleteContaContabilTO extends AutocompleteTO {

	Integer classificacao;
	Integer tipoEspecial;

	public Integer getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(Integer classificacao) {
		this.classificacao = classificacao;
	}

	public Integer getTipoEspecial() {
		return tipoEspecial;
	}

	public void setTipoEspecial(Integer tipoEspecial) {
		this.tipoEspecial = tipoEspecial;
	}
}

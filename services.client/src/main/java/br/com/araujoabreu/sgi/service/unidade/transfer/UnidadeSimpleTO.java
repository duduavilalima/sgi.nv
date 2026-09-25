package br.com.araujoabreu.sgi.service.unidade.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaTO;

//FIXME: Objetos "simples" tem que ter poucos atributos.
public class UnidadeSimpleTO extends PessoaJuridicaTO {
	private String codnome;
	private String tipoUnidade;
	private Long codigoCidade;

	public UnidadeSimpleTO() {

	}

	public UnidadeSimpleTO(Long codigo) {
		setCodigo(codigo);
	}

	public UnidadeSimpleTO(Long codigo, String codnome) {
		setCodigo(codigo);
		this.codnome = codnome;
	}

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

	public Long getCodigoCidade() {
		return codigoCidade;
	}

	public void setCodigoCidade(Long codigoCidade) {
		this.codigoCidade = codigoCidade;
	}

}

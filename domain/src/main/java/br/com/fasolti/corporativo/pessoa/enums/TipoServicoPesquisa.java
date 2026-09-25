package br.com.fasolti.corporativo.pessoa.enums;

/**
 * Tipo de serviço da pesquisa.
 *
 * @author danieloliveira
 */
public enum TipoServicoPesquisa {

	SERASA_Experian("SERASA/Experian"),
	NF_E("NF-e (RFB)");

	private final String nome;

	private TipoServicoPesquisa(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return this.nome;
	}

}

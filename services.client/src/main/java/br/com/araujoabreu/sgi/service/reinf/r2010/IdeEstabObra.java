package br.com.araujoabreu.sgi.service.reinf.r2010;

import br.com.araujoabreu.sgi.service.reinf.IdeContri;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class IdeEstabObra {//Identificação do Estabelecimento/obra contratante dos Serviços

	private int tpInscEstab = IdeContri.TipoInscricao.CNPJ.getValor(); //Preencher com o código correspondente ao tipo de inscrição
	private String nrInscEstab; /* Informar o número de inscrição do estabelecimento de acordo com o tipo de
								inscrição indicado no campo {tpInscEstab}.
								Se {indObra} = [0] preencher com CNPJ do contribuinte declarante.
								Se {indObra} = [1] informar um número de CNO de propriedade do {cnpjPrestador}.
								Se {indObra} = [2] informar um número de CNO de propriedade do contribuinte declarante.
								Validação: A inscrição informada deve ser compatível com o {tpInscEstab}.
								Se {indObra} = [0], o número informado deve ser um CNPJ. Se {indObra} for igual a [1,2] o número informado deve ser um CNO.
								*/
	private int indObra; /* Indicativo de Prestação de Serviços em Obra de Construção Civil:
							0 - Não é obra de construção civil ou não está sujeita a matrícula de obra;
							1 - Obra de Construção Civil - Empreitada Total;
							2 - Obra de Construção Civil - Empreitada Parcial.
						 */
	private IdePrestServ idePrestServ; // Identificação do prestador de serviços mediante cessão de mão de obra ou empreitada.

	public int getTpInscEstab() {
		return tpInscEstab;
	}

	public void setTpInscEstab(int tpInscEstab) {
		this.tpInscEstab = tpInscEstab;
	}

	public String getNrInscEstab() {
		return nrInscEstab;
	}

	public void setNrInscEstab(String nrInscEstab) {
		this.nrInscEstab = nrInscEstab;
	}

	public int getIndObra() {
		return indObra;
	}

	public void setIndObra(int indObra) {
		this.indObra = indObra;
	}

	public IdePrestServ getIdePrestServ() {
		return idePrestServ;
	}

	public void setIdePrestServ(IdePrestServ idePrestServ) {
		this.idePrestServ = idePrestServ;
	}

	@JsonIgnore
	public String getDescricaoIndObra() {
		for (TipoObra tp : TipoObra.values()) {
			if (tp.ordinal() == this.indObra)
				return tp.descricao;
		}

		return null;
	}

	public enum TipoObra {
		NAO_CIVIL(0, "Não é obra de construção civil ou não está sujeita a matrícula de obra"),
		CIVIL_EMPREITADA_TOTAL(1, "Obra de Construção Civil - Empreitada Total"),
		CIVIL_EMPREITADA_PARCIAL(2, "Obra de Construção Civil - Empreitada Parcial");

		final int valor;
		final String descricao;

		TipoObra(int valor, String descricao) {
			this.valor = valor;
			this.descricao = descricao;
		}

		public int getValor() {
			return valor;
		}
	}
}

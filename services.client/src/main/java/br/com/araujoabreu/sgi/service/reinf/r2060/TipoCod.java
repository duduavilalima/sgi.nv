package br.com.araujoabreu.sgi.service.reinf.r2060;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

public class TipoCod {

	private String codAtivEcon;
	private String vlrRecBrutaAtiv; //Preencher com o valor total da receita da atividade.
	private String vlrExcRecBruta;  //Preencher com o Valor total das Exclusões da Receita Bruta previstas em lei.
	private String vlrAdicRecBruta; //Preencher com o Valor total das Adições da Receita Bruta previstas em lei
	private String vlrBcCPRB;       //Preencher com o Valor da Base de Cálculo da Contribuição Previdenciária sobre a Receita Bruta.
	private String vlrCPRBapur;     //Preencher com o Valor Contribuição Previdenciária sobre a Receita Bruta.

	@JacksonXmlElementWrapper(useWrapping = false)
	private List<TipoAjuste> tipoAjuste;

	public String getCodAtivEcon() {
		return codAtivEcon;
	}

	public void setCodAtivEcon(String codAtivEcon) {
		this.codAtivEcon = codAtivEcon;
	}

	public String getVlrRecBrutaAtiv() {
		return vlrRecBrutaAtiv;
	}

	public void setVlrRecBrutaAtiv(String vlrRecBrutaAtiv) {
		this.vlrRecBrutaAtiv = vlrRecBrutaAtiv;
	}

	public String getVlrExcRecBruta() {
		return vlrExcRecBruta;
	}

	public void setVlrExcRecBruta(String vlrExcRecBruta) {
		this.vlrExcRecBruta = vlrExcRecBruta;
	}

	public String getVlrAdicRecBruta() {
		return vlrAdicRecBruta;
	}

	public void setVlrAdicRecBruta(String vlrAdicRecBruta) {
		this.vlrAdicRecBruta = vlrAdicRecBruta;
	}

	public String getVlrBcCPRB() {
		return vlrBcCPRB;
	}

	public void setVlrBcCPRB(String vlrBcCPRB) {
		this.vlrBcCPRB = vlrBcCPRB;
	}

	public String getVlrCPRBapur() {
		return vlrCPRBapur;
	}

	public void setVlrCPRBapur(String vlrCPRBapur) {
		this.vlrCPRBapur = vlrCPRBapur;
	}

	public List<TipoAjuste> getTipoAjuste() {
		return tipoAjuste;
	}

	public void setTipoAjuste(List<TipoAjuste> tipoAjuste) {
		this.tipoAjuste = tipoAjuste;
	}
}

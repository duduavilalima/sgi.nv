package br.com.araujoabreu.sgi.service.reinf.r2060;

public class IdeEstab {

	private int tpInscEstab = 1;
	private String nrInscEstab;
	private String vlrRecBrutaTotal; //Valor da Receita Bruta Total do Estabelecimento no Período.
	private String vlrCPApurTotal;   //Valor total da Contribuição Previdenciária sobre Receita Bruta apurada pelo Estabelecimento no período.
	private String vlrCPRBSuspTotal; //Valor da Contribuição Previdenciária com exigibilidade suspensa.
	private TipoCod tipoCod;

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

	public String getVlrRecBrutaTotal() {
		return vlrRecBrutaTotal;
	}

	public void setVlrRecBrutaTotal(String vlrRecBrutaTotal) {
		this.vlrRecBrutaTotal = vlrRecBrutaTotal;
	}

	public String getVlrCPApurTotal() {
		return vlrCPApurTotal;
	}

	public void setVlrCPApurTotal(String vlrCPApurTotal) {
		this.vlrCPApurTotal = vlrCPApurTotal;
	}

	public String getVlrCPRBSuspTotal() {
		return vlrCPRBSuspTotal;
	}

	public void setVlrCPRBSuspTotal(String vlrCPRBSuspTotal) {
		this.vlrCPRBSuspTotal = vlrCPRBSuspTotal;
	}

	public TipoCod getTipoCod() {
		return tipoCod;
	}

	public void setTipoCod(TipoCod tipoCod) {
		this.tipoCod = tipoCod;
	}
}

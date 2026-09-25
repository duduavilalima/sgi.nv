package br.com.araujoabreu.sgi.service.reinf.retorno;

public class RPrest {

	private String tpInscTomador;
	private Double nrInscTomador;
	private Double vlrTotalBaseRet;
	private Double vlrTotalRetPrinc;
	private Double vlrTotalRetAdic;
	private Double vlrTotalNRetPrinc;
	private Double vlrTotalNRetAdic;

	public String getTpInscTomador() {
		return tpInscTomador;
	}

	public void setTpInscTomador(String tpInscTomador) {
		this.tpInscTomador = tpInscTomador;
	}

	public Double getNrInscTomador() {
		return nrInscTomador;
	}

	public void setNrInscTomador(Double nrInscTomador) {
		this.nrInscTomador = nrInscTomador;
	}

	public Double getVlrTotalBaseRet() {
		return vlrTotalBaseRet;
	}

	public void setVlrTotalBaseRet(Double vlrTotalBaseRet) {
		this.vlrTotalBaseRet = vlrTotalBaseRet;
	}

	public Double getVlrTotalRetPrinc() {
		return vlrTotalRetPrinc;
	}

	public void setVlrTotalRetPrinc(Double vlrTotalRetPrinc) {
		this.vlrTotalRetPrinc = vlrTotalRetPrinc;
	}

	public Double getVlrTotalRetAdic() {
		return vlrTotalRetAdic;
	}

	public void setVlrTotalRetAdic(Double vlrTotalRetAdic) {
		this.vlrTotalRetAdic = vlrTotalRetAdic;
	}

	public Double getVlrTotalNRetPrinc() {
		return vlrTotalNRetPrinc;
	}

	public void setVlrTotalNRetPrinc(Double vlrTotalNRetPrinc) {
		this.vlrTotalNRetPrinc = vlrTotalNRetPrinc;
	}

	public Double getVlrTotalNRetAdic() {
		return vlrTotalNRetAdic;
	}

	public void setVlrTotalNRetAdic(Double vlrTotalNRetAdic) {
		this.vlrTotalNRetAdic = vlrTotalNRetAdic;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("RPrest{");
		sb.append("tpInscTomador='").append(tpInscTomador).append('\'');
		sb.append(", nrInscTomador=").append(nrInscTomador);
		sb.append(", vlrTotalBaseRet=").append(vlrTotalBaseRet);
		sb.append(", vlrTotalRetPrinc=").append(vlrTotalRetPrinc);
		sb.append(", vlrTotalRetAdic=").append(vlrTotalRetAdic);
		sb.append(", vlrTotalNRetPrinc=").append(vlrTotalNRetPrinc);
		sb.append(", vlrTotalNRetAdic=").append(vlrTotalNRetAdic);
		sb.append('}');
		return sb.toString();
	}
}

package br.com.araujoabreu.sgi.service.reinf.r1070;

public class IdeProcesso {
	private int tpProc;
	private String nrProc;
	private String iniValid;
	private String fimValid;
	private int indAutoria;
	private InfoSusp infoSusp;
	private DadosProcJud dadosProcJud;

	public int getTpProc() {
		return tpProc;
	}

	public void setTpProc(int tpProc) {
		this.tpProc = tpProc;
	}

	public String getNrProc() {
		return nrProc;
	}

	public void setNrProc(String nrProc) {
		this.nrProc = nrProc;
	}

	public String getIniValid() {
		return iniValid;
	}

	public void setIniValid(String iniValid) {
		this.iniValid = iniValid;
	}

	public String getFimValid() {
		return fimValid;
	}

	public void setFimValid(String fimValid) {
		this.fimValid = fimValid;
	}

	public int getIndAutoria() {
		return indAutoria;
	}

	public void setIndAutoria(int indAutoria) {
		this.indAutoria = indAutoria;
	}

	public InfoSusp getInfoSusp() {
		return infoSusp;
	}

	public void setInfoSusp(InfoSusp infoSusp) {
		this.infoSusp = infoSusp;
	}

	public DadosProcJud getDadosProcJud() {
		return dadosProcJud;
	}

	public void setDadosProcJud(DadosProcJud dadosProcJud) {
		this.dadosProcJud = dadosProcJud;
	}

	public enum TipoProcesso {
		ADMINISTRATIVO(1), JUDICIAL(2);

		final int valor;

		TipoProcesso(int valor) {
			this.valor = valor;
		}

		public int getValor() {
			return valor;
		}
	}

	public enum IndAutoriaProcesso {
		PROPRIO(0), OUTRA_ENTIDADE(1);

		final int valor;

		IndAutoriaProcesso(int valor) {
			this.valor = valor;
		}

		public int getValor() {
			return valor;
		}
	}
}

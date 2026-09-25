package br.com.araujoabreu.sgi.service.reinf.r1000;

public class IdeEvento {
	private int tpAmb = br.com.araujoabreu.sgi.service.reinf.IdeEvento.TipoAmbiente.PRODUCAO_RESTRITA.getValor();
	private int procEmi = 1; //Processo de emissão do evento: 1 - Aplicativo do contribuinte; 2 - Aplicativo governamental.
	private String verProc;


	public int getTpAmb() {
		return tpAmb;
	}

	public void setTpAmb(int tpAmb) {
		this.tpAmb = tpAmb;
	}

	public int getProcEmi() {
		return procEmi;
	}

	public void setProcEmi(int procEmi) {
		this.procEmi = procEmi;
	}

	public String getVerProc() {
		return verProc;
	}

	public void setVerProc(String verProc) {
		this.verProc = verProc;
	}
}

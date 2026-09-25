package br.com.araujoabreu.sgi.service.reinf.r2099;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "IdeEvento")
public class IdeEvento {
	private String perApur;  // Informar o ano/mês de referência das informações no formato AAAA-MM
	private int tpAmb = br.com.araujoabreu.sgi.service.reinf.IdeEvento.TipoAmbiente.PRODUCAO_RESTRITA.getValor();
	private int procEmi = 1; //Processo de emissão do evento: 1 - Aplicativo do contribuinte; 2 - Aplicativo governamental.
	private String verProc;

	public String getPerApur() {
		return perApur;
	}

	public void setPerApur(String perApur) {
		this.perApur = perApur;
	}

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

package br.com.araujoabreu.sgi.service.reinf.retorno;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoTotal {

	private String nrRecArqBase;
	private RPrest rPrest;


	public String getNrRecArqBase() {
		return nrRecArqBase;
	}

	public void setNrRecArqBase(String nrRecArqBase) {
		this.nrRecArqBase = nrRecArqBase;
	}

	public RPrest getrPrest() {
		return rPrest;
	}

	public void setrPrest(RPrest rPrest) {
		this.rPrest = rPrest;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("InfoTotal{");
		sb.append("nrRecArqBase='").append(nrRecArqBase).append('\'');
		sb.append(", rPrest=").append(rPrest);
		sb.append('}');
		return sb.toString();
	}
}

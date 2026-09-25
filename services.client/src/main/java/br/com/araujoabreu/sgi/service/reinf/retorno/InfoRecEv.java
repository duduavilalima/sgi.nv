package br.com.araujoabreu.sgi.service.reinf.retorno;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoRecEv {

	private String nrProtEntr;
	private Date dhProcess;
	private String tpEv;
	private String idEv;
	private String hash;


	public String getNrProtEntr() {
		return nrProtEntr;
	}

	public void setNrProtEntr(String nrProtEntr) {
		this.nrProtEntr = nrProtEntr;
	}

	public Date getDhProcess() {
		return dhProcess;
	}

	public void setDhProcess(Date dhProcess) {
		this.dhProcess = dhProcess;
	}

	public String getTpEv() {
		return tpEv;
	}

	public void setTpEv(String tpEv) {
		this.tpEv = tpEv;
	}

	public String getIdEv() {
		return idEv;
	}

	public void setIdEv(String idEv) {
		this.idEv = idEv;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("InfoRecEv{");
		sb.append("dhProcess=").append(dhProcess);
		sb.append(", tpEv='").append(tpEv).append('\'');
		sb.append(", idEv='").append(idEv).append('\'');
		sb.append(", hash='").append(hash).append('\'');
		sb.append('}');
		return sb.toString();
	}
}

package br.com.araujoabreu.sgi.service.contaCorrente.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class FaixaChequeTO extends BaseTO {

	private Long de;
	private Long ate;
	private String desc;

	public FaixaChequeTO() {
	}

	public FaixaChequeTO(Long de, Long ate, String desc) {
		this.de = de;
		this.ate = ate;
		this.desc = desc;
	}

	public Long getDe() {
		return de;
	}

	public void setDe(Long de) {
		this.de = de;
	}

	public Long getAte() {
		return ate;
	}

	public void setAte(Long ate) {
		this.ate = ate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}

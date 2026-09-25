/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.araujoabreu.sgi.service.reinf.retorno;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author eduardo.avila
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegOcorrs {
	private int tpOcorr;
	private String codResp;
	private String dscResp;

	public int getTpOcorr() {
		return tpOcorr;
	}

	public void setTpOcorr(int tpOcorr) {
		this.tpOcorr = tpOcorr;
	}

	public String getCodResp() {
		return codResp;
	}

	public void setCodResp(String codResp) {
		this.codResp = codResp;
	}

	public String getDscResp() {
		return dscResp;
	}

	public void setDscResp(String dscResp) {
		this.dscResp = dscResp;
	}


}

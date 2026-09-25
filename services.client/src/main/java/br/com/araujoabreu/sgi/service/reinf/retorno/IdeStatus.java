/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.araujoabreu.sgi.service.reinf.retorno;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

/**
 * @author eduardo.avila
 */
public class IdeStatus {
	private int cdRetorno;
	private String descRetorno;

	@JacksonXmlElementWrapper(useWrapping = false)
	private List<RegOcorrs> regOcorrs;

	public int getCdRetorno() {
		return cdRetorno;
	}

	public void setCdRetorno(int cdRetorno) {
		this.cdRetorno = cdRetorno;
	}

	public String getDescRetorno() {
		return descRetorno;
	}

	public void setDescRetorno(String descRetorno) {
		this.descRetorno = descRetorno;
	}

	public List<RegOcorrs> getRegOcorrs() {
		return regOcorrs;
	}

	public void setRegOcorrs(List<RegOcorrs> regOcorrs) {
		this.regOcorrs = regOcorrs;
	}
}

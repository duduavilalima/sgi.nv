/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.araujoabreu.sgi.service.reinf.retorno;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author eduardo.avila
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class EvtTotal {

	@JacksonXmlProperty(isAttribute = true)
	private String id;
	private IdeEvento ideEvento;
	private IdeContri ideContri;
	private IdeRecRetorno ideRecRetorno;
	private InfoRecEv infoRecEv;
	private InfoTotal infoTotal;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public IdeEvento getIdeEvento() {
		return ideEvento;
	}

	public void setIdeEvento(IdeEvento ideEvento) {
		this.ideEvento = ideEvento;
	}

	public IdeContri getIdeContri() {
		return ideContri;
	}

	public void setIdeContri(IdeContri ideContri) {
		this.ideContri = ideContri;
	}

	public IdeRecRetorno getIdeRecRetorno() {
		return ideRecRetorno;
	}

	public void setIdeRecRetorno(IdeRecRetorno ideRecRetorno) {
		this.ideRecRetorno = ideRecRetorno;
	}

	public InfoRecEv getInfoRecEv() {
		return infoRecEv;
	}

	public void setInfoRecEv(InfoRecEv infoRecEv) {
		this.infoRecEv = infoRecEv;
	}

	public InfoTotal getInfoTotal() {
		return infoTotal;
	}

	public void setInfoTotal(InfoTotal infoTotal) {
		this.infoTotal = infoTotal;
	}
}

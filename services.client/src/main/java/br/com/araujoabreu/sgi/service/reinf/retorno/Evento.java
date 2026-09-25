/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.araujoabreu.sgi.service.reinf.retorno;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author eduardo.avila
 */
public class Evento {

	@JacksonXmlProperty(isAttribute = true)
	private String id;

	@JacksonXmlProperty(localName = "Reinf")
	private ReinfEvento reinfEvento;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ReinfEvento getReinfEvento() {
		return reinfEvento;
	}

	public void setReinfEvento(ReinfEvento reinfEvento) {
		this.reinfEvento = reinfEvento;
	}


}

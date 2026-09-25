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
public class IdeTransmissor {
	@JacksonXmlProperty(localName = "IdTransmissor")
	private String idTransmissor;

	public String getIdTransmissor() {
		return idTransmissor;
	}

	public void setIdTransmissor(String idTransmissor) {
		this.idTransmissor = idTransmissor;
	}


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.araujoabreu.sgi.service.reinf.retorno;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author eduardo.avila
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "Reinf")
public class ReinfEvento {
	private EvtTotal evtTotal;

	public EvtTotal getEvtTotal() {
		return evtTotal;
	}

	public void setEvtTotal(EvtTotal evtTotal) {
		this.evtTotal = evtTotal;
	}


}

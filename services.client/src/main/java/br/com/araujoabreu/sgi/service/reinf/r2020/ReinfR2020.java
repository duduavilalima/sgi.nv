package br.com.araujoabreu.sgi.service.reinf.r2020;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Reinf")
public class ReinfR2020 {
	private EvtServPrest evtServPrest;

	public EvtServPrest getEvtServPrest() {
		return evtServPrest;
	}

	public void setEvtServPrest(EvtServPrest evtServPrest) {
		this.evtServPrest = evtServPrest;
	}
}

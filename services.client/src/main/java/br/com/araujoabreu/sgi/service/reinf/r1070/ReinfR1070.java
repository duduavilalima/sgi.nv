package br.com.araujoabreu.sgi.service.reinf.r1070;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Reinf")
public class ReinfR1070 {

	EvtTabProcesso evtTabProcesso;

	public EvtTabProcesso getEvtTabProcesso() {
		return evtTabProcesso;
	}

	public void setEvtTabProcesso(EvtTabProcesso evtTabProcesso) {
		this.evtTabProcesso = evtTabProcesso;
	}
}

package br.com.araujoabreu.sgi.service.reinf.r2060;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Reinf")
public class ReinfR2060 {
	private EvtCPRB evtCPRB;

	public EvtCPRB getEvtCPRB() {
		return evtCPRB;
	}

	public void setEvtCPRB(EvtCPRB evtCPRB) {
		this.evtCPRB = evtCPRB;
	}
}

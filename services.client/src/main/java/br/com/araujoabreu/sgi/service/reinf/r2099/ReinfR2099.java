package br.com.araujoabreu.sgi.service.reinf.r2099;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Reinf")
public class ReinfR2099 {
	private EvtFechaEvPer evtFechaEvPer;

	public EvtFechaEvPer getEvtFechaEvPer() {
		return evtFechaEvPer;
	}

	public void setEvtFechaEvPer(EvtFechaEvPer evtFechaEvPer) {
		this.evtFechaEvPer = evtFechaEvPer;
	}
}

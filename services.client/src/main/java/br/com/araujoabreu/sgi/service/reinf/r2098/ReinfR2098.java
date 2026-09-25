package br.com.araujoabreu.sgi.service.reinf.r2098;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Reinf")
public class ReinfR2098 {

	private EvtReabreEvPer evtReabreEvPer;

	public EvtReabreEvPer getEvtReabreEvPer() {
		return evtReabreEvPer;
	}

	public void setEvtReabreEvPer(EvtReabreEvPer evtReabreEvPer) {
		this.evtReabreEvPer = evtReabreEvPer;
	}
}

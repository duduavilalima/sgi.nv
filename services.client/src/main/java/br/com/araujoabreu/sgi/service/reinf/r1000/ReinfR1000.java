package br.com.araujoabreu.sgi.service.reinf.r1000;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Reinf")
public class ReinfR1000 {
	private EvtInfoContri evtInfoContri;

	public EvtInfoContri getEvtInfoContri() {
		return evtInfoContri;
	}

	public void setEvtInfoContri(EvtInfoContri evtInfoContri) {
		this.evtInfoContri = evtInfoContri;
	}
}

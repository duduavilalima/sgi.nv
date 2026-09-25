package br.com.araujoabreu.sgi.service.reinf.r2098;

import br.com.araujoabreu.sgi.service.reinf.IdeContri;
import br.com.araujoabreu.sgi.service.reinf.IdeEvento;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "EvtReabreEvPer")
public class EvtReabreEvPer {

	@JacksonXmlProperty(isAttribute = true)
	private String id; //Identificação única do evento
	private IdeEvento ideEvento; //Informações de Identificação do Evento
	private IdeContri ideContri; //Informações de identificação do contribuinte

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
}

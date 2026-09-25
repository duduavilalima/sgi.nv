package br.com.araujoabreu.sgi.service.reinf.r1070;

import br.com.araujoabreu.sgi.service.reinf.IdeContri;
import br.com.araujoabreu.sgi.service.reinf.r1000.IdeEvento;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "EvtTabProcesso")
public class EvtTabProcesso {

	@JacksonXmlProperty(isAttribute = true)
	private String id; //Identificação única do evento
	private IdeEvento ideEvento;
	private IdeContri ideContri;
	private InfoProcesso infoProcesso;

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

	public InfoProcesso getInfoProcesso() {
		return infoProcesso;
	}

	public void setInfoProcesso(InfoProcesso infoProcesso) {
		this.infoProcesso = infoProcesso;
	}
}

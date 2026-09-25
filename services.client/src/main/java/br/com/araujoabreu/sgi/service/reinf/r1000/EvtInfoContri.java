package br.com.araujoabreu.sgi.service.reinf.r1000;

import br.com.araujoabreu.sgi.service.reinf.IdeContri;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName = "EvtInfoContri")
public class EvtInfoContri {

	@JacksonXmlProperty(isAttribute = true)
	private String id; //Identificação única do evento
	private IdeEvento ideEvento; //Informações de Identificação do Evento
	private IdeContri ideContri; //Informações de identificação do contribuinte
	private InfoContri infoContri;

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

	public InfoContri getInfoContri() {
		return infoContri;
	}

	public void setInfoContri(InfoContri infoContri) {
		this.infoContri = infoContri;
	}
}

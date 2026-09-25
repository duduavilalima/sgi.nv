package br.com.araujoabreu.sgi.service.reinf.r2010;


import br.com.araujoabreu.sgi.service.reinf.IdeContri;
import br.com.araujoabreu.sgi.service.reinf.IdeEvento;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "EvtServTom")
public class EvtServTom {

	@JacksonXmlProperty(isAttribute = true)
	private String id; //Identificação única do evento
	private IdeEvento ideEvento; //Informações de Identificação do Evento
	private IdeContri ideContri; //Informações de identificação do contribuinte
	private InfoServTom infoServTom; //Serviços Tomados - Cessão de Mão de Obra ou Empreitada

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

	public InfoServTom getInfoServTom() {
		return infoServTom;
	}

	public void setInfoServTom(InfoServTom infoServTom) {
		this.infoServTom = infoServTom;
	}
}

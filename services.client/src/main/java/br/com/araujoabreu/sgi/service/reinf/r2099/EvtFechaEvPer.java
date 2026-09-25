package br.com.araujoabreu.sgi.service.reinf.r2099;

import br.com.araujoabreu.sgi.service.reinf.IdeContri;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class EvtFechaEvPer {

	@JacksonXmlProperty(isAttribute = true)
	private String id;
	private IdeEvento ideEvento;
	private IdeContri ideContri;
	private IdeRespInf ideRespInf;
	private InfoFech infoFech;


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

	public InfoFech getInfoFech() {
		return infoFech;
	}

	public void setInfoFech(InfoFech infoFech) {
		this.infoFech = infoFech;
	}

	public IdeRespInf getIdeRespInf() {
		return ideRespInf;
	}

	public void setIdeRespInf(IdeRespInf ideRespInf) {
		this.ideRespInf = ideRespInf;
	}
}

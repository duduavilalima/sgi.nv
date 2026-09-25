/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.araujoabreu.sgi.service.reinf.retorno;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

/**
 * @author eduardo.avila
 */
public class RetornoLoteEventos {

	@JacksonXmlProperty(isAttribute = true)
	private String id;
	private IdeTransmissor ideTransmissor;
	private Status status;
	private List<Evento> retornoEventos;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public IdeTransmissor getIdeTransmissor() {
		return ideTransmissor;
	}

	public void setIdeTransmissor(IdeTransmissor ideTransmissor) {
		this.ideTransmissor = ideTransmissor;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Evento> getRetornoEventos() {
		return retornoEventos;
	}

	public void setRetornoEventos(List<Evento> retornoEventos) {
		this.retornoEventos = retornoEventos;
	}

	@Override
	public String toString() {
		return "RetornoLoteEventos{" + "id=" + id + ", ideTransmissor=" + ideTransmissor + ", status=" + status + ", retornoEventos=" + retornoEventos + '}';
	}


}

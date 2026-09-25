/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.araujoabreu.sgi.service.reinf.retorno;

/**
 * @author eduardo.avila
 */
public class Reinf {
	private RetornoLoteEventos retornoLoteEventos;

	public RetornoLoteEventos getRetornoLoteEventos() {
		return retornoLoteEventos;
	}

	public void setRetornoLoteEventos(RetornoLoteEventos retornoLoteEventos) {
		this.retornoLoteEventos = retornoLoteEventos;
	}

	@Override
	public String toString() {
		return "Reinf{" + "retornoLoteEventos=" + retornoLoteEventos + '}';
	}


}

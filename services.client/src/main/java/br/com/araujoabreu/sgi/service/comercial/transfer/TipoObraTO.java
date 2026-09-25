package br.com.araujoabreu.sgi.service.comercial.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class TipoObraTO extends BaseTO {

	private String nome;

	public TipoObraTO() {
	}

	public TipoObraTO(Long codigo) {
		setCodigo(codigo);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}

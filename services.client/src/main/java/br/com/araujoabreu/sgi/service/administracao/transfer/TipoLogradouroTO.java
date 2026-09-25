package br.com.araujoabreu.sgi.service.administracao.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class TipoLogradouroTO extends BaseTO {

	private String nome;

	public TipoLogradouroTO() {
	}

	public TipoLogradouroTO(Long codigo) {
		setCodigo(codigo);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}

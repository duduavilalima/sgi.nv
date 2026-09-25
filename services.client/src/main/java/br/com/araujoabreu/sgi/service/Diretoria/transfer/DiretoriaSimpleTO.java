package br.com.araujoabreu.sgi.service.Diretoria.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class DiretoriaSimpleTO extends BaseTO {

	private String nome;
	private String numero;
	private String descricao;

	public DiretoriaSimpleTO() {
	}

	public DiretoriaSimpleTO(Long codigo) {
		setCodigo(codigo);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}

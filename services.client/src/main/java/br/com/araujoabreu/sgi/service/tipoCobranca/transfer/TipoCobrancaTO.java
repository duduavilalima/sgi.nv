package br.com.araujoabreu.sgi.service.tipoCobranca.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class TipoCobrancaTO extends BaseTO {
	private String chave;
	private String nome;
	private Integer ordem;
	private String descricao;

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}

package br.com.araujoabreu.sgi.service.contato.transfer;

import br.com.araujoabreu.sgi.service.security.transfer.PerfilTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class RelacionamentoContatoPesquisaTO extends BaseSearchTO {

	private String nome;
	private PerfilTO perfil;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public PerfilTO getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilTO perfil) {
		this.perfil = perfil;
	}
}

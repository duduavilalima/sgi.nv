package br.com.araujoabreu.sgi.service.etapaFluxo;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.security.transfer.PerfilTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class EtapaFluxoPesquisaTO extends BaseSearchTO {

	private String nome;
	private Integer ordem;
	private Integer statusAtual;
	private Integer statusBase;
	private ColaboradorTO colaborador;
	private PerfilTO perfil;

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

	public Integer getStatusAtual() {
		return statusAtual;
	}

	public void setStatusAtual(Integer statusAtual) {
		this.statusAtual = statusAtual;
	}

	public Integer getStatusBase() {
		return statusBase;
	}

	public void setStatusBase(Integer statusBase) {
		this.statusBase = statusBase;
	}

	public ColaboradorTO getColaborador() {
		return colaborador;
	}

	public void setColaborador(ColaboradorTO colaborador) {
		this.colaborador = colaborador;
	}

	public PerfilTO getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilTO perfil) {
		this.perfil = perfil;
	}
}

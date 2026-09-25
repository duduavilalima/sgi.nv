package br.com.araujoabreu.sgi.service.administracao.transfer;

import br.com.araujoabreu.sgi.service.etapaFluxo.EtapaFluxoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.List;

public class FluxoTO extends BaseTO {

	private String nome;
	private String descricao;
	private String tipoFluxo;
	private String situacao;
	private List<EtapaFluxoTO> listaEtapasFluxo;

	public FluxoTO() {
	}

	public FluxoTO(Long codigo) {
		setCodigo(codigo);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipoFluxo() {
		return tipoFluxo;
	}

	public void setTipoFluxo(String tipoFluxo) {
		this.tipoFluxo = tipoFluxo;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public List<EtapaFluxoTO> getListaEtapasFluxo() {
		return listaEtapasFluxo;
	}

	public void setListaEtapasFluxo(List<EtapaFluxoTO> listaEtapasFluxo) {
		this.listaEtapasFluxo = listaEtapasFluxo;
	}
}

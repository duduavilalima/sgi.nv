package br.com.araujoabreu.sgi.service.motivoRetorno.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MotivoAplicacaoTO extends BaseTO {
	private String nome;
	private String chave;
	private String tipoMotivo;
	private List<MotivoRetornoTO> listaMotivosRetorno = new ArrayList<>();

	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

	public MotivoAplicacaoTO() {
	}

	public MotivoAplicacaoTO(Long codigo) {
		setCodigo(codigo);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getTipoMotivo() {
		return tipoMotivo;
	}

	public void setTipoMotivo(String tipoMotivo) {
		this.tipoMotivo = tipoMotivo;
	}

	public List<MotivoRetornoTO> getListaMotivosRetorno() {
		return listaMotivosRetorno;
	}

	public void setListaMotivosRetorno(List<MotivoRetornoTO> listaMotivosRetorno) {
		this.listaMotivosRetorno = listaMotivosRetorno;
	}

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getAtualizador() {
		return atualizador;
	}

	public void setAtualizador(String atualizador) {
		this.atualizador = atualizador;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
}

package br.com.araujoabreu.sgi.service.contato.transfer;

import br.com.araujoabreu.sgi.service.security.transfer.PerfilTO;
import br.com.araujoabreu.sgi.service.util.AuditoriaBasicaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;

public class RelacionamentoContatoTO extends BaseTO implements AuditoriaBasicaTO {

	private String nome;
	private PerfilTO perfil;
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;
	private String nomeCriador;
	private String nomeAtualizador;

	public RelacionamentoContatoTO() {
	}

	public RelacionamentoContatoTO(Long codigo) {
		this.setCodigo(codigo);
	}

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

	@Override
	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	@Override
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Override
	public String getAtualizador() {
		return atualizador;
	}

	public void setAtualizador(String atualizador) {
		this.atualizador = atualizador;
	}

	@Override
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public String getNomeCriador() {
		return nomeCriador;
	}

	public void setNomeCriador(String nomeCriador) {
		this.nomeCriador = nomeCriador;
	}

	public String getNomeAtualizador() {
		return nomeAtualizador;
	}

	public void setNomeAtualizador(String nomeAtualizador) {
		this.nomeAtualizador = nomeAtualizador;
	}
}

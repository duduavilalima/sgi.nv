package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;


@Entity
public class Tipo extends AuditEntity implements DomainObject, Serializable {

	private static final long serialVersionUID = -2514242580445207885L;
	private String chave;
	private String descricao;
	private String nome;
	private Integer ordem;
	private String subtipo;
	private String tabela;
	private Situacao situacao;

	public Tipo() {
	}

	public String getChave() {
		return this.chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	public Integer getOrdem() {
		return this.ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}


	public String getSubtipo() {
		return this.subtipo;
	}

	public void setSubtipo(String subtipo) {
		this.subtipo = subtipo;
	}


	public String getTabela() {
		return this.tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_situacao")
	public Situacao getSituacao() {
		return this.situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Tipo)) return false;
		if (!super.equals(o)) return false;
		Tipo tipo = (Tipo) o;
		return Objects.equals(getChave(), tipo.getChave()) &&
			Objects.equals(getDescricao(), tipo.getDescricao()) &&
			Objects.equals(getNome(), tipo.getNome()) &&
			Objects.equals(getOrdem(), tipo.getOrdem()) &&
			Objects.equals(getSubtipo(), tipo.getSubtipo()) &&
			Objects.equals(getTabela(), tipo.getTabela()) &&
			Objects.equals(getSituacao(), tipo.getSituacao());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getChave(), getDescricao(), getNome(), getOrdem(), getSubtipo(), getTabela(), getSituacao());
	}
}

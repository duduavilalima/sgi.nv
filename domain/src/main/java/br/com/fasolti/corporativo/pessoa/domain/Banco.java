package br.com.fasolti.corporativo.pessoa.domain;

import br.com.araujoabreu.sgi.enumerators.Situacao;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.pessoa.interf.Dominio;
import org.hibernate.Hibernate;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "BANCO")
public class Banco implements Dominio, DomainObject, Comparable<Banco> {

	private static final long serialVersionUID = 4259380059133631548L;

	private Long codigo;

	private String nome;
	private String descricao;
	private Situacao situacao = Situacao.ATIVO;

	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

	public Banco() {
	}

	public Banco(Long codigo) {
		setCodigo(codigo);
	}

	public Banco(final Long codigo, final String nome) {
		setCodigo(codigo);
		this.nome = nome;
	}

	public Banco(String nome, Situacao situacao) {
		this.nome = nome;
		this.situacao = situacao;
	}

	public Banco(String nome, String descricao, Situacao situacao) {
		this.nome = nome;
		this.descricao = descricao;
		this.situacao = situacao;
	}

	@Id
	@Column(name = "CODIGO", nullable = false)
	@Override
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Transient
	public Long getId() {
		return getCodigo();
	}

	public void setId(Long id) {
		setCodigo(id);
	}

	@Transient
	public boolean isNew() {
		return null == getCodigo();
	}

	@Override
	@Column(name = "NOME", nullable = false, length = 50)
	@NotNull
	@Length(max = 50)
	public String getNome() {
		return this.nome;
	}

	@Override
	public void setNome(final String nome) {
		this.nome = nome;
	}

	@Column(name = "SITUACAO", nullable = false)
	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Column(name = "DESCRICAO", nullable = false, length = 50)
	@NotNull
	@Length(max = 50)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "CRIADOR", updatable = false)
	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CRIACAO", updatable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Column(name = "ATUALIZADOR", insertable = false)
	public String getAtualizador() {
		return atualizador;
	}

	public void setAtualizador(String atualizador) {
		this.atualizador = atualizador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_ATUALIZACAO", insertable = false)
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), nome);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(Banco.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Banco other = (Banco) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (nome == null) {
			if (other.getNome() != null) {
				return false;
			}
		} else if (!nome.equals(other.getNome())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Banco [codigo=" + getCodigo() + ", nome=" + nome + "]";
	}

	@Transient
	public String getCodigoFormatado() {
		if (getCodigo() != null)
			return String.format("%03d", getCodigo());
		return "";
	}

	@Override
	public int compareTo(Banco o) {
		return o.getNome().compareTo(this.getNome());
	}
}

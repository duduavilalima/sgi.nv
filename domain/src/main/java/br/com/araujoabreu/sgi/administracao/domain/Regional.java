package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "REGIONAL")
@SequenceGenerator(name="SEQ_REGIONAL", sequenceName="SEQ_REGIONAL", allocationSize=1)
public class Regional extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 3924451300270205231L;
	private Diretoria diretoria;
	private String descricao;
	private Situacao situacao;
	private List<RegionalGerencia> listaGerencias;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_diretoria")
	public Diretoria getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(Diretoria diretoria) {
		this.diretoria = diretoria;
	}

	@Column(name = "descricao", nullable = false)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "situacao", nullable = false)
	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "regional")
	public List<RegionalGerencia> getListaGerencias() {
		return listaGerencias;
	}

	public void setListaGerencias(List<RegionalGerencia> listaGerencias) {
		this.listaGerencias = listaGerencias;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Regional)) return false;
		if (!super.equals(o)) return false;
		Regional regional = (Regional) o;
		return getDescricao().equals(regional.getDescricao()) &&
			getSituacao() == regional.getSituacao();
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getDescricao(), getSituacao());
	}

	public enum Situacao {
		INATIVO("Inativo"),
		ATIVO("Ativo");

		private final String nome;

		Situacao(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}
	}
}

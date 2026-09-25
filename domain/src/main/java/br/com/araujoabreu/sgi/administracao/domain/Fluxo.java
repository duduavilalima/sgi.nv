package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.enumerators.Situacao;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "FLUXO")
@SequenceGenerator(name = "SEQ_FLUXO", sequenceName = "SEQ_FLUXO", allocationSize = 1)
public class Fluxo extends AuditEntity implements DomainObject, Serializable {

	private static final long serialVersionUID = -3832338406852781459L;
	private String nome;
	private String descricao;
	private TipoFluxo tipoFluxo;
	private Situacao situacao;
	private List<EtapaFluxo> listaEtapasFluxo;

	public Fluxo() {
	}

	public Fluxo(Long codigo) {
		setCodigo(codigo);
	}

	public Fluxo(final Long codigo, final String nome) {
		setCodigo(codigo);
		this.nome = nome;
	}

	@Column(name = "NOME", nullable = false, length = 15)
	@NotNull
	@Length(max = 15)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	@Column(name = "TIPO")
	public TipoFluxo getTipoFluxo() {
		return tipoFluxo;
	}

	public void setTipoFluxo(TipoFluxo tipoFluxo) {
		this.tipoFluxo = tipoFluxo;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fluxo")
	public List<EtapaFluxo> getListaEtapasFluxo() {
		return listaEtapasFluxo;
	}

	public void setListaEtapasFluxo(List<EtapaFluxo> listaEtapasFluxo) {
		this.listaEtapasFluxo = listaEtapasFluxo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fluxo other = (Fluxo) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Fluxo [codigo=" + getCodigo() + ", nome=" + nome + ", descricao="
			+ descricao + "]";
	}

	public enum TipoFluxo {
		CONTRATO_MANUTENCAO("Contrato de manutenção"),
		CONTRATO_INSTALACAO("Contrato de Instalações");

		private final String descricao;

		private TipoFluxo(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}

	public enum SituacaoFluxo {
		ATIVO("Ativo"),
		INATIVO("Inativo");

		private final String descricao;

		private SituacaoFluxo(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}

}

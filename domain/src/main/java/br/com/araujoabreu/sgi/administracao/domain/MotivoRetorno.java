package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro.SituacaoCentroFinanceiro;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.hibernate.Hibernate;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MOTIVO_RETORNO")
@SequenceGenerator(name="SEQ_MOTIVO_RETORNO", sequenceName="SEQ_MOTIVO_RETORNO", allocationSize = 1)
public class MotivoRetorno extends AuditEntity implements DomainObject, Serializable, Comparable<MotivoRetorno> {

	private static final long serialVersionUID = -6944376675650952101L;
	private String motivo;
	private String solucao;
	private SituacaoCentroFinanceiro situacao = SituacaoCentroFinanceiro.ATIVO;

	private MotivoAplicacao motivoAplicacao;

	private Pessoa inativador;
	private Date dataInativacao;

	private List<MotivoDocumento> listaDocumento;

	public MotivoRetorno() {
	}

	public MotivoRetorno(Long codigo) {
		setCodigo(codigo);
	}

	public MotivoRetorno(String motivo, String solucao, SituacaoCentroFinanceiro situacao) {
		super();
		this.motivo = motivo;
		this.solucao = solucao;
		this.situacao = situacao;
	}

	public MotivoRetorno(String motivo, String solucao, SituacaoCentroFinanceiro situacao, MotivoAplicacao motivoAplicacao, Pessoa inativador, Date dataInativacao) {
		super();
		this.motivo = motivo;
		this.solucao = solucao;
		this.situacao = situacao;
		this.motivoAplicacao = motivoAplicacao;
		this.inativador = inativador;
		this.dataInativacao = dataInativacao;
	}

	@Column(name = "MOTIVO")
	@Length(max = 255)
	@NotNull
	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	@Column(name = "SOLUCAO")
	@Length(max = 2000)
	@NotNull
	public String getSolucao() {
		return solucao;
	}

	public void setSolucao(String solucao) {
		this.solucao = solucao;
	}

	@Column(name = "SITUACAO", nullable = false)
	@NotNull
	public SituacaoCentroFinanceiro getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoCentroFinanceiro situacao) {
		this.situacao = situacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_APLICACAO_MOTIVO")
	public MotivoAplicacao getMotivoAplicacao() {
		return motivoAplicacao;
	}

	public void setMotivoAplicacao(MotivoAplicacao motivoAplicacao) {
		this.motivoAplicacao = motivoAplicacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_INATIVADOR")
	public Pessoa getInativador() {
		return inativador;
	}

	public void setInativador(Pessoa inativador) {
		this.inativador = inativador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_INATIVACAO")
	public Date getDataInativacao() {
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motivoRetorno")
	public List<MotivoDocumento> getListaDocumento() {
		return listaDocumento;
	}

	public void setListaDocumento(List<MotivoDocumento> listaDocumento) {
		this.listaDocumento = listaDocumento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(MotivoRetorno.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		MotivoRetorno other = (MotivoRetorno) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(MotivoRetorno o) {
		return this.motivo.compareTo(o.getMotivo());
	}

	@Override
	public String toString() {
		return "MotivoRetorno [codigo=" + getCodigo() + ", motivo=" + motivo + ", solucao=" + solucao + ", situacao=" + situacao + "]";
	}

}

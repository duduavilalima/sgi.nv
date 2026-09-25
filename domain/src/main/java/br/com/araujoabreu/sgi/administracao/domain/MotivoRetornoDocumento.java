package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "MOTIVO_RETORNO_DOCUMENTO")
@SequenceGenerator(name = "SEQ_MOTIVO_RETORNO_DOCUMENTO", sequenceName = "SEQ_MOTIVO_RETORNO_DOCUMENTO", allocationSize = 1)
public class MotivoRetornoDocumento extends AuditEntity implements DomainObject, Serializable {

	private static final long serialVersionUID = -2056072682213043800L;
	private String observacao;
	private Pessoa pessoa;
	private MotivoAplicacao aplicacao;

	private List<MotivoDocumento> listaMotivoRetorno;
	private List<MotivoRetornoDemonstrativo> listaMotivoRetornoDemonstrativo;
	private List<MotivoRetornoOrdemCompra> listaMotivoRetornoOC;
	private List<MotivoRetornoRequisicaoCompra> listaMotivoRetornoRC;
	private List<MotivoRetornoNota> listaMotivoRetornoNota;
	private Set<MotivoRetornoDocumentoContrato> motivoRetornoDocumentoContratos = new HashSet<>(0);
	private List<MotivoRetornoDocumentoFaturamento> listaMotivoRetornoDocumentoFaturamento;
	private List<MotivoRetornoDocumentoEtapa> listaMotivoRetornoDocumentoEtapa;
	private List<MotivoRetornoDocumentoNotaDebito> listaMotivoRetornoDocumentoNotaDebito;

	public MotivoRetornoDocumento() {
		super();
	}

	public MotivoRetornoDocumento(Long codigo) {
		setCodigo(codigo);
	}

	public MotivoRetornoDocumento(List<MotivoDocumento> listaMotivoRetorno) {
		super();
		this.listaMotivoRetorno = listaMotivoRetorno;
	}

	@Column(name = "OBSERVACAO")
	@Length(max = 2000)
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PESSOA")
	@NotNull
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motivoRetornoDocumento")
	public List<MotivoDocumento> getListaMotivoRetorno() {
		return listaMotivoRetorno;
	}

	public void setListaMotivoRetorno(List<MotivoDocumento> listaMotivoRetorno) {
		this.listaMotivoRetorno = listaMotivoRetorno;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motivoRetornoDocumento")
	public List<MotivoRetornoDemonstrativo> getListaMotivoRetornoDemonstrativo() {
		return listaMotivoRetornoDemonstrativo;
	}

	public void setListaMotivoRetornoDemonstrativo(List<MotivoRetornoDemonstrativo> listaMotivoRetornoDemonstrativo) {
		this.listaMotivoRetornoDemonstrativo = listaMotivoRetornoDemonstrativo;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motivoRetornoDocumento")
	public List<MotivoRetornoOrdemCompra> getListaMotivoRetornoOC() {
		return listaMotivoRetornoOC;
	}

	public void setListaMotivoRetornoOC(List<MotivoRetornoOrdemCompra> listaMotivoRetornoOC) {
		this.listaMotivoRetornoOC = listaMotivoRetornoOC;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motivoRetornoDocumento")
	public List<MotivoRetornoNota> getListaMotivoRetornoNota() {
		return listaMotivoRetornoNota;
	}

	public void setListaMotivoRetornoNota(List<MotivoRetornoNota> listaMotivoRetornoNota) {
		this.listaMotivoRetornoNota = listaMotivoRetornoNota;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motivoRetornoDocumento")
	public List<MotivoRetornoRequisicaoCompra> getListaMotivoRetornoRC() {
		return listaMotivoRetornoRC;
	}

	public void setListaMotivoRetornoRC(List<MotivoRetornoRequisicaoCompra> listaMotivoRetornoRC) {
		this.listaMotivoRetornoRC = listaMotivoRetornoRC;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "motivoRetornoDocumento")
	public Set<MotivoRetornoDocumentoContrato> getMotivoRetornoDocumentoContratos() {
		return this.motivoRetornoDocumentoContratos;
	}

	public void setMotivoRetornoDocumentoContratos(Set<MotivoRetornoDocumentoContrato> motivoRetornoDocumentoContratos) {
		this.motivoRetornoDocumentoContratos = motivoRetornoDocumentoContratos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "motivoRetornoDocumento")
	public List<MotivoRetornoDocumentoFaturamento> getListaMotivoRetornoDocumentoFaturamento() {
		return listaMotivoRetornoDocumentoFaturamento;
	}

	public void setListaMotivoRetornoDocumentoFaturamento(List<MotivoRetornoDocumentoFaturamento> listaMotivoRetornoDocumentoFaturamento) {
		this.listaMotivoRetornoDocumentoFaturamento = listaMotivoRetornoDocumentoFaturamento;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "motivoRetornoDocumento")
	public List<MotivoRetornoDocumentoEtapa> getListaMotivoRetornoDocumentoEtapa() {
		return listaMotivoRetornoDocumentoEtapa;
	}

	public void setListaMotivoRetornoDocumentoEtapa(List<MotivoRetornoDocumentoEtapa> listaMotivoRetornoDocumentoEtapa) {
		this.listaMotivoRetornoDocumentoEtapa = listaMotivoRetornoDocumentoEtapa;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_APLICACAO_MOTIVO")
	public MotivoAplicacao getAplicacao() {
		return aplicacao;
	}

	public void setAplicacao(MotivoAplicacao aplicacao) {
		this.aplicacao = aplicacao;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "motivoRetornoDocumento")
	public List<MotivoRetornoDocumentoNotaDebito> getListaMotivoRetornoDocumentoNotaDebito() {
		return listaMotivoRetornoDocumentoNotaDebito;
	}

	public void setListaMotivoRetornoDocumentoNotaDebito(List<MotivoRetornoDocumentoNotaDebito> listaMotivoRetornoDocumentoNotaDebito) {
		this.listaMotivoRetornoDocumentoNotaDebito = listaMotivoRetornoDocumentoNotaDebito;
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

		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		MotivoRetornoDocumento other = (MotivoRetornoDocumento) obj;
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
	public String toString() {
		return "MotivoRetornoDocumento [codigo=" + getCodigo() + ", observacao=" + observacao + "]";
	}

	@Transient
	public boolean isPreencimentoValido() {
		if (this.observacao != null) {
			this.observacao = this.observacao.trim();
		}

		if ((this.listaMotivoRetorno == null || this.listaMotivoRetorno.isEmpty())
			&& (this.listaMotivoRetornoDemonstrativo == null || this.listaMotivoRetornoDemonstrativo.isEmpty())
			&& (this.listaMotivoRetornoNota == null || this.listaMotivoRetornoNota.isEmpty())
			&& (this.listaMotivoRetornoOC == null || this.listaMotivoRetornoOC.isEmpty())
			&& (this.listaMotivoRetornoRC == null || this.listaMotivoRetornoRC.isEmpty()) && StringUtils.isEmpty(this.observacao)) {
			return false;
		}

		return true;
	}

}

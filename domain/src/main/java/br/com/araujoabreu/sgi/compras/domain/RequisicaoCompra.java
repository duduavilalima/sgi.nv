package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro.EscopoCompra;
import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoRequisicaoCompra;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import org.hibernate.Hibernate;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Uma <code>RequisicaoCompra</code> determina o conjunto de itens que precisam ser
 * adquiridos pela gerência.
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "REQUISICAO_COMPRA")
@SequenceGenerator(name="SEQ_REQUISICAO_COMPRA", sequenceName="SEQ_REQUISICAO_COMPRA", allocationSize=1)
public class RequisicaoCompra extends AuditEntity {

	private static final long serialVersionUID = 284979354705962769L;
	private CentroFinanceiro centroFinanceiro;
	private String numero;
	private Boolean urgente = false;
	private Boolean individual = false;
	private String justificativa;
	private Status status = Status.ABERTA;
	private NaturezaAquisicao naturezaAquisicao;
	private PessoaFisica solicitante;
	private Colaborador aprovador;
	private Colaborador cancelador;
	private MeioContato meioContatoEntrega;
	private List<RequisicaoCompraRefinada> requisicoesCompraRefinadas = new ArrayList<>();
	private EscopoCompra escopoCompra;
	private Date dataLimite;
	private Date dataEnvioAprovacao;
	private Date dataCancelamento;
	private TipoCobranca tipoCobranca;
	private List<MotivoRetornoRequisicaoCompra> motivosRetorno;

	public RequisicaoCompra(CentroFinanceiro centroFinanceiro) {
		super();
		this.centroFinanceiro = centroFinanceiro;
	}

	public RequisicaoCompra(Long codigo) {
		super();
		setCodigo(codigo);
	}

	public RequisicaoCompra() {
		super();
	}

	@Transient
	public boolean isAberta() {
		return Status.ABERTA.equals(getStatus());
	}

	@Transient
	public boolean isReAberta() {
		return Status.REABERTA.equals(getStatus());
	}

	@Transient
	public boolean isAutorizada() {
		return Status.AUTORIZADA.equals(getStatus());
	}

	public void setCodigo(Long codigo) {
		setCodigo(codigo);
		if (codigo != null) {
			this.numero = "RC/" + codigo;
		}
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	@NotNull
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@Column(name = "NUMERO", nullable = false, length = 14)
	@NotNull
	@Length(max = 14)
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "URGENTE", nullable = false, length = 1)
	@NotNull
	public Boolean getUrgente() {
		return urgente;
	}

	public void setUrgente(Boolean urgente) {
		this.urgente = urgente;
	}

	@Column(name = "JUSTIFICATIVA")
	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	@Column(name = "STATUS")
	@NotNull
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NATUREZA_AQUISICAO")
	@NotNull
	public NaturezaAquisicao getNaturezaAquisicao() {
		return naturezaAquisicao;
	}

	public void setNaturezaAquisicao(NaturezaAquisicao naturezaAquisicao) {
		this.naturezaAquisicao = naturezaAquisicao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_SOLICITANTE")
	@NotNull
	public PessoaFisica getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(PessoaFisica solicitante) {
		this.solicitante = solicitante;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESP_CANCELAMENTO")
	public Colaborador getCancelador() {
		return cancelador;
	}

	public void setCancelador(Colaborador cancelador) {
		this.cancelador = cancelador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_APROVADOR")
	public Colaborador getAprovador() {
		return aprovador;
	}

	public void setAprovador(Colaborador aprovador) {
		this.aprovador = aprovador;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CODIGO_MEIO_CONTATO_ENTREGA")
	@NotNull
	public MeioContato getMeioContatoEntrega() {
		return meioContatoEntrega;
	}

	public void setMeioContatoEntrega(MeioContato meioContatoEntrega) {
		this.meioContatoEntrega = meioContatoEntrega;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requisicaoCompra")
	public List<RequisicaoCompraRefinada> getRequisicoesCompraRefinadas() {
		return requisicoesCompraRefinadas;
	}

	public void setRequisicoesCompraRefinadas(List<RequisicaoCompraRefinada> requisicoesCompraRefinadas) {
		this.requisicoesCompraRefinadas = requisicoesCompraRefinadas;
	}

	@Column(name = "ESCOPO_COMPRA")
	@NotNull
	public EscopoCompra getEscopoCompra() {
		return escopoCompra;
	}

	public void setEscopoCompra(EscopoCompra escopoCompra) {
		this.escopoCompra = escopoCompra;
	}

	@Column(name = "INDIVIDUAL", nullable = false, length = 1)
	@NotNull
	public Boolean getIndividual() {
		return individual;
	}

	public void setIndividual(Boolean individual) {
		this.individual = individual;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requisicaoCompra")
	public List<MotivoRetornoRequisicaoCompra> getMotivosRetorno() {
		return motivosRetorno;
	}

	public void setMotivosRetorno(List<MotivoRetornoRequisicaoCompra> motivosRetorno) {
		this.motivosRetorno = motivosRetorno;
	}

	/**
	 * Determina a data limite para o recebimento dos itens da requisição de compra.
	 * Informação utlizada para priorizar e orientar o comprador
	 *
	 * @return
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_LIMITE", nullable = false)
	@NotNull
	public Date getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(Date dataLimite) {
		this.dataLimite = dataLimite;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CANCELAMENTO")
	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	/**
	 * Determina a data de entrada da RC no processo de aprovação.
	 *
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_ENVIO_APROVACAO")
	public Date getDataEnvioAprovacao() {
		return dataEnvioAprovacao;
	}

	public void setDataEnvioAprovacao(Date dataEnvioAprovacao) {
		this.dataEnvioAprovacao = dataEnvioAprovacao;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_TIPO_COBRANCA")
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(RequisicaoCompra.class.isAssignableFrom(Hibernate.getClass(obj))))
			return false;
		RequisicaoCompra other = (RequisicaoCompra) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;
		if (numero == null) {
			if (other.getNumero() != null)
				return false;
		} else if (!numero.equals(other.getNumero()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RequisicaoCompra [codigo=" + getCodigo() + ", numero=" + numero + "]";
	}

	public enum Status {
		ABERTA("Aberta"), REABERTA("Reaberta"), AUTORIZACAO("Em autorização"), AUTORIZADA("Autorizada"), CANCELADA("Cancelada");

		private final String nome;

		Status(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}
	}

}

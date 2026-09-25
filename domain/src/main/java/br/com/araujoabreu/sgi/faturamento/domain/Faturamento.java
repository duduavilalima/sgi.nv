package br.com.araujoabreu.sgi.faturamento.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumentoFaturamento;
import br.com.araujoabreu.sgi.administracao.domain.ServicoTipo;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.TipoDocumentoPagamento;
import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.hibernate.annotations.Cascade;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FATURAMENTO")
@SequenceGenerator(name="SEQ_FATURAMENTO", sequenceName="SEQ_FATURAMENTO", allocationSize=1)
public class Faturamento extends AuditEntity implements DomainObject, Serializable {

	private static final long serialVersionUID = -2345055618745797092L;
	private Timestamp dataCancelamento;
	private String discriminacaoCentral;
	private String discriminacaoFinal;
	private String discriminacaoInicial;
	private String identificacao;
	private Boolean issRetidoCliente;
	private BigDecimal percentualServico;
	private String periodo;
	private Status situacao;
	private TipoFaturamento tipo;
	private TipoDocumentoPagamento tipoDocumentoPagamento;
	private BigDecimal valor;
	private CentroFinanceiro centroFinanceiro;
	private ContaCorrente contaCorrente;
	private Pessoa cliente;
	private Pessoa cancelador;
	private TipoCobrancaFaturamento tipoCobrancaFaturamento;
	private Date dataVencimento1;
	private Date dataVencimento2;
	private List<FaturamentoAliquota> faturamentoAliquotas;
	private List<FaturamentoEtapa> faturamentoEtapas;
	private List<MotivoRetornoDocumentoFaturamento> motivosRetornoDocumentoFaturamento;
	private List<FaturamentoRetencao> faturamentoRetencoes;
	private Cidade cidadePrestacaoServico;
	private ServicoTipo servicoTipo;
	private String resumo;
	private TipoFornecimentoNF tipoFornecimento;
	private TipoDocumentoFaturamento tipoDocumentoFaturamento;

	public Faturamento() {
	}

	public Faturamento(Long codigo) {
		setCodigo(codigo);
	}

	@Column(name = "DATA_CANCELAMENTO")
	public Timestamp getDataCancelamento() {
		return this.dataCancelamento;
	}

	public void setDataCancelamento(Timestamp dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	@Column(name = "DISCRIMINACAO_CENTRAL", length = 2000)
	public String getDiscriminacaoCentral() {
		return this.discriminacaoCentral;
	}

	public void setDiscriminacaoCentral(String discriminacaoCentral) {
		this.discriminacaoCentral = discriminacaoCentral;
	}

	@Column(name = "DISCRIMINACAO_FINAL", nullable = false, length = 2000)
	public String getDiscriminacaoFinal() {
		return this.discriminacaoFinal;
	}

	public void setDiscriminacaoFinal(String discriminacaoFinal) {
		this.discriminacaoFinal = discriminacaoFinal;
	}

	@Column(name = "DISCRIMINACAO_INICIAL", nullable = false, length = 2000)
	public String getDiscriminacaoInicial() {
		return this.discriminacaoInicial;
	}

	public void setDiscriminacaoInicial(String discriminacaoInicial) {
		this.discriminacaoInicial = discriminacaoInicial;
	}

	@Column(length = 100)
	public String getIdentificacao() {
		return this.identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	@Column(name = "ISS_RETIDO_CLIENTE", nullable = false)
	public Boolean getIssRetidoCliente() {
		return this.issRetidoCliente;
	}

	public void setIssRetidoCliente(Boolean issRetidoCliente) {
		this.issRetidoCliente = issRetidoCliente;
	}

	@Column(name = "PERCENTUAL_SERVICO", nullable = false, precision = 5, scale = 2)
	public BigDecimal getPercentualServico() {
		return this.percentualServico;
	}

	public void setPercentualServico(BigDecimal percentualServico) {
		this.percentualServico = percentualServico;
	}

	@Column(length = 50)
	public String getPeriodo() {
		return this.periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	@Column(nullable = false)
	public Status getSituacao() {
		return this.situacao;
	}

	public void setSituacao(Status situacao) {
		this.situacao = situacao;
	}

	@Column(nullable = false)
	public TipoFaturamento getTipo() {
		return this.tipo;
	}

	public void setTipo(TipoFaturamento tipo) {
		this.tipo = tipo;
	}

	@Column(name = "TIPO_DOCUMENTO_PAGAMENTO", nullable = false)
	public TipoDocumentoPagamento getTipoDocumentoPagamento() {
		return this.tipoDocumentoPagamento;
	}

	public void setTipoDocumentoPagamento(TipoDocumentoPagamento tipoDocumentoPagamento) {
		this.tipoDocumentoPagamento = tipoDocumentoPagamento;
	}

	@Column(nullable = false, precision = 15, scale = 2)
	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	//bi-directional many-to-one association to CentroFinanceiro
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_centro_financeiro", nullable = false)
	public CentroFinanceiro getCentroFinanceiro() {
		return this.centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	//bi-directional many-to-one association to ContaCorrente
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_conta_corrente", nullable = false)
	public ContaCorrente getContaCorrente() {
		return this.contaCorrente;
	}

	public void setContaCorrente(ContaCorrente contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CANCELAMENTO")
	public Pessoa getCancelador() {
		return this.cancelador;
	}

	public void setCancelador(Pessoa cancelador) {
		this.cancelador = cancelador;
	}

	//bi-directional many-to-one association to Pessoa
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_cliente", nullable = false)
	public Pessoa getCliente() {
		return this.cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	//bi-directional many-to-one association to TipoCobrancaFaturamento
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_tipo_cobranca", nullable = false)
	public TipoCobrancaFaturamento getTipoCobrancaFaturamento() {
		return this.tipoCobrancaFaturamento;
	}

	public void setTipoCobrancaFaturamento(TipoCobrancaFaturamento tipoCobrancaFaturamento) {
		this.tipoCobrancaFaturamento = tipoCobrancaFaturamento;
	}

	//bi-directional many-to-one association to FaturamentoAliquota
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "faturamento")
	public List<FaturamentoAliquota> getFaturamentoAliquotas() {
		return this.faturamentoAliquotas;
	}

	public void setFaturamentoAliquotas(List<FaturamentoAliquota> faturamentoAliquotas) {
		this.faturamentoAliquotas = faturamentoAliquotas;
	}

	public FaturamentoAliquota addFaturamentoAliquota(FaturamentoAliquota faturamentoAliquota) {
		getFaturamentoAliquotas().add(faturamentoAliquota);
		faturamentoAliquota.setFaturamento(this);

		return faturamentoAliquota;
	}

	public FaturamentoAliquota removeFaturamentoAliquota(FaturamentoAliquota faturamentoAliquota) {
		getFaturamentoAliquotas().remove(faturamentoAliquota);
		faturamentoAliquota.setFaturamento(null);

		return faturamentoAliquota;
	}

	//bi-directional many-to-one association to FaturamentoEtapa
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "faturamento")
	@Cascade({org.hibernate.annotations.CascadeType.ALL})
	@OrderBy(value = "codigo desc")
	public List<FaturamentoEtapa> getFaturamentoEtapas() {
		return this.faturamentoEtapas;
	}

	public void setFaturamentoEtapas(List<FaturamentoEtapa> faturamentoEtapas) {
		this.faturamentoEtapas = faturamentoEtapas;
	}

	public FaturamentoEtapa addFaturamentoEtapa(FaturamentoEtapa faturamentoEtapa) {
		getFaturamentoEtapas().add(faturamentoEtapa);
		faturamentoEtapa.setFaturamento(this);

		return faturamentoEtapa;
	}

	public FaturamentoEtapa removeFaturamentoEtapa(FaturamentoEtapa faturamentoEtapa) {
		getFaturamentoEtapas().remove(faturamentoEtapa);
		faturamentoEtapa.setFaturamento(null);

		return faturamentoEtapa;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "faturamento")
	public List<MotivoRetornoDocumentoFaturamento> getMotivosRetornoDocumentoFaturamento() {
		return this.motivosRetornoDocumentoFaturamento;
	}

	public void setMotivosRetornoDocumentoFaturamento(List<MotivoRetornoDocumentoFaturamento> motivoRetornoDocumentoFaturamentos) {
		this.motivosRetornoDocumentoFaturamento = motivoRetornoDocumentoFaturamentos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "faturamento")
	public List<FaturamentoRetencao> getFaturamentoRetencoes() {
		return faturamentoRetencoes;
	}

	public void setFaturamentoRetencoes(List<FaturamentoRetencao> faturamentoRetencoes) {
		this.faturamentoRetencoes = faturamentoRetencoes;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_cidade_prestacao_servicos")
	public Cidade getCidadePrestacaoServico() {
		return cidadePrestacaoServico;
	}

	public void setCidadePrestacaoServico(Cidade cidadePrestacaoServico) {
		this.cidadePrestacaoServico = cidadePrestacaoServico;
	}

	@Column(name = "DATA_VENCIMENTO_1")
	public Date getDataVencimento1() {
		return dataVencimento1;
	}

	public void setDataVencimento1(Date dataVencimento1) {
		this.dataVencimento1 = dataVencimento1;
	}

	@Column(name = "DATA_VENCIMENTO_2")
	public Date getDataVencimento2() {
		return dataVencimento2;
	}

	public void setDataVencimento2(Date dataVencimento2) {
		this.dataVencimento2 = dataVencimento2;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_SERVICO_TIPO")
	public ServicoTipo getServicoTipo() {
		return servicoTipo;
	}

	public void setServicoTipo(ServicoTipo servicoTipo) {
		this.servicoTipo = servicoTipo;
	}

	@Column(name = "RESUMO")
	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	@Column(name = "TIPO_FORNECIMENTO_NF", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public TipoFornecimentoNF getTipoFornecimento() {
		return tipoFornecimento;
	}

	public void setTipoFornecimento(TipoFornecimentoNF tipoFornecimento) {
		this.tipoFornecimento = tipoFornecimento;
	}

	@Column(name = "TIPO_DOCUMENTO_FATURAMENTO", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public TipoDocumentoFaturamento getTipoDocumentoFaturamento() {
		return tipoDocumentoFaturamento;
	}

	public void setTipoDocumentoFaturamento(TipoDocumentoFaturamento tipoDocumentoFaturamento) {
		this.tipoDocumentoFaturamento = tipoDocumentoFaturamento;
	}

	@Transient
	public String getNumero() {
		if (this.getCodigo() != null)
			return "FT/" + String.format("%06d", this.getCodigo());

		return "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getCodigo() == null) ? 0 : this.getCodigo().hashCode());
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

		if (!(obj instanceof Faturamento)) {
			return false;
		}
		Faturamento other = (Faturamento) obj;
		if (this.getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!this.getCodigo().equals(other.getCodigo()))
			return false;
		return true;
	}

	public enum Status {
		ABERTO("Aberto", "Aberto"),
		EM_FATURAMENTO("Em faturamento (uma etapa com NF)", "Em faturamento"),
		EM_RECEBIMENTO("Em recebimento (uma etapa com NF recebida)", "Em recebimento"),
		RECEBIDO("Recebido (todas as etapas com as NFs recebidas)", "Recebido"),
		CANCELADO("Cancelado", "Cancelado");

		private final String descricao;
		private final String nome;

		Status(String descricao, String nome) {
			this.descricao = descricao;
			this.nome = nome;
		}

		public String getDescricao() {
			return descricao;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return getNome();
		}
	}

	public enum TipoFaturamento {
		CONTRATUAL("Contratual"),
		EXTRA_CONTRATUAL("Extra contratual");

		private final String descricao;

		TipoFaturamento(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return getDescricao();
		}
	}

	public enum TipoDocumentoFaturamento {
		NOTA_FISCAL("Nota fiscal"),
		NOTA_DEBITO("Nota de débito");

		private final String descricao;

		TipoDocumentoFaturamento(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return getDescricao();
		}
	}

	public enum TipoFornecimentoNF {
		MATERIAL("Só material"),
		SERVICO("Só serviço"),
		SERVICO_MATERIAL("Serviço/Material");

		private final String descricao;

		TipoFornecimentoNF(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return getDescricao();
		}
	}

}

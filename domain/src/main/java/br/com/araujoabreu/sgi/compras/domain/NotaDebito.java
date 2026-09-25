package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.*;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoEtapa;
import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "NOTA_DEBITO")
public class NotaDebito extends AuditEntity implements DomainObject, Serializable {
	private static final long serialVersionUID = 1L;

	private Unidade empresa;
	private CentroFinanceiro centroFinanceiro;
	private Pessoa cliente;
	private ServicoTipo servicoTipo;
	private Cidade cidade;
	private ContaCorrente contaCorrentePrevista;
	private ContaCorrente contaCorrenteRecebimento;
	private Pessoa responsavelNumeracao;
	private Pessoa responsavelCancelarNumeracao;
	private Pessoa responsavelRecebimento;
	private Pessoa responsavelCancelarRecebimento;
	private Pessoa responsavelCancelamento;
	private Pessoa responsavelReverterCancelamento;

	private String numero;
	private SituacaoNotaDebito situacao;
	private Date dataEmissao;
	private Date dataVencimento;
	private Date dataPagamento;
	private Date dataAgendamento;
	private Date dataContabilizacao;
	private String descricao;

	private BigDecimal valorBruto = BigDecimal.ZERO;
	private BigDecimal valorTibutoPrevisto = BigDecimal.ZERO;
	private BigDecimal valorDescontoPrevisto = BigDecimal.ZERO;
	private BigDecimal valorDevolucaoPrevisto = BigDecimal.ZERO;
	private BigDecimal valorLiquidoPrevisto = BigDecimal.ZERO;
	private BigDecimal valorMulta = BigDecimal.ZERO;
	private BigDecimal valorJuros = BigDecimal.ZERO;
	private BigDecimal valorTributoRecebido = BigDecimal.ZERO;
	private BigDecimal valorDescontoRecebido = BigDecimal.ZERO;
	private BigDecimal valorDevolucaoRecebido = BigDecimal.ZERO;
	private BigDecimal valorLiquidoRecebido = BigDecimal.ZERO;

	private Pagamento.TipoDocumentoPagamento tipoDocumentoPrevisto;
	private Pagamento.TipoDocumentoPagamento tipoDocumentoRecebimento;

	private String protocoloEntrega;

	private Date dataResponsavelNumeracao;
	private Date dataResponsavelCancelarNumeracao;
	private Date dataResponsavelRecebimento;
	private Date dataResponsavelCancelarRecebimento;
	private Date dataResponsavelCancelamento;
	private Date dataResponsavelReverterCancelamento;

	private List<FaturamentoEtapa> faturamentoEtapas;
	private List<MotivoRetornoDocumentoNotaDebito> motivosRetornoDocumentoNotaDebito;

	private int etapaNumero;
	private String etapaDescricao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_empresa", nullable = false)
	public Unidade getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Unidade empresa) {
		this.empresa = empresa;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_centro_financeiro", nullable = false)
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_cliente", nullable = false)
	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_servico_tipo")
	public ServicoTipo getServicoTipo() {
		return servicoTipo;
	}

	public void setServicoTipo(ServicoTipo servicoTipo) {
		this.servicoTipo = servicoTipo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_cidade_prestacao_servicos")
	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_conta_corrente_prevista", nullable = false)
	public ContaCorrente getContaCorrentePrevista() {
		return contaCorrentePrevista;
	}

	public void setContaCorrentePrevista(ContaCorrente contaCorrentePrevista) {
		this.contaCorrentePrevista = contaCorrentePrevista;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_conta_corrente_recebimento")
	public ContaCorrente getContaCorrenteRecebimento() {
		return contaCorrenteRecebimento;
	}

	public void setContaCorrenteRecebimento(ContaCorrente contaCorrenteRecebimento) {
		this.contaCorrenteRecebimento = contaCorrenteRecebimento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_resp_numeracao")
	public Pessoa getResponsavelNumeracao() {
		return responsavelNumeracao;
	}

	public void setResponsavelNumeracao(Pessoa responsavelNumeracao) {
		this.responsavelNumeracao = responsavelNumeracao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_resp_canc_numeracao")
	public Pessoa getResponsavelCancelarNumeracao() {
		return responsavelCancelarNumeracao;
	}

	public void setResponsavelCancelarNumeracao(Pessoa responsavelCancelarNumeracao) {
		this.responsavelCancelarNumeracao = responsavelCancelarNumeracao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_resp_recebimento")
	public Pessoa getResponsavelRecebimento() {
		return responsavelRecebimento;
	}

	public void setResponsavelRecebimento(Pessoa responsavelRecebimento) {
		this.responsavelRecebimento = responsavelRecebimento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_resp_canc_recebimento")
	public Pessoa getResponsavelCancelarRecebimento() {
		return responsavelCancelarRecebimento;
	}

	public void setResponsavelCancelarRecebimento(Pessoa responsavelCancelarRecebimento) {
		this.responsavelCancelarRecebimento = responsavelCancelarRecebimento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_resp_cancelamento")
	public Pessoa getResponsavelCancelamento() {
		return responsavelCancelamento;
	}

	public void setResponsavelCancelamento(Pessoa responsavelCancelamento) {
		this.responsavelCancelamento = responsavelCancelamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_resp_reversao_cancelamento")
	public Pessoa getResponsavelReverterCancelamento() {
		return responsavelReverterCancelamento;
	}

	public void setResponsavelReverterCancelamento(Pessoa responsavelReverterCancelamento) {
		this.responsavelReverterCancelamento = responsavelReverterCancelamento;
	}

	@Column(name = "numero", nullable = false)
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "situacao", nullable = false)
	public SituacaoNotaDebito getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoNotaDebito situacao) {
		this.situacao = situacao;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_emissao", nullable = false)
	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_vencimento", nullable = false)
	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_pagamento", nullable = false)
	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_agendamento", nullable = false)
	public Date getDataAgendamento() {
		return dataAgendamento;
	}

	public void setDataAgendamento(Date dataAgendamento) {
		this.dataAgendamento = dataAgendamento;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "data_contabilizacao")
	public Date getDataContabilizacao() {
		return dataContabilizacao;
	}

	public void setDataContabilizacao(Date dataContabilizacao) {
		this.dataContabilizacao = dataContabilizacao;
	}

	@Column(name = "descricao", nullable = false)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "valor_bruto", nullable = false)
	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	@Column(name = "valor_tributo_previsto", nullable = false)
	public BigDecimal getValorTibutoPrevisto() {
		return valorTibutoPrevisto;
	}

	public void setValorTibutoPrevisto(BigDecimal valorTibutoPrevisto) {
		this.valorTibutoPrevisto = valorTibutoPrevisto;
	}

	@Column(name = "valor_desconto_previsto", nullable = false)
	public BigDecimal getValorDescontoPrevisto() {
		return valorDescontoPrevisto;
	}

	public void setValorDescontoPrevisto(BigDecimal valorDescontoPrevisto) {
		this.valorDescontoPrevisto = valorDescontoPrevisto;
	}

	@Column(name = "valor_devolucao_previsto", nullable = false)
	public BigDecimal getValorDevolucaoPrevisto() {
		return valorDevolucaoPrevisto;
	}

	public void setValorDevolucaoPrevisto(BigDecimal valorDevolucaoPrevisto) {
		this.valorDevolucaoPrevisto = valorDevolucaoPrevisto;
	}

	@Column(name = "valor_liquido_previsto", nullable = false)
	public BigDecimal getValorLiquidoPrevisto() {
		return valorLiquidoPrevisto;
	}

	public void setValorLiquidoPrevisto(BigDecimal valorLiquidoPrevisto) {
		this.valorLiquidoPrevisto = valorLiquidoPrevisto;
	}

	@Column(name = "valor_multa", nullable = false)
	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	@Column(name = "valor_juros", nullable = false)
	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	@Column(name = "valor_tributo_recebido", nullable = false)
	public BigDecimal getValorTributoRecebido() {
		return valorTributoRecebido;
	}

	public void setValorTributoRecebido(BigDecimal valorTributoRecebido) {
		this.valorTributoRecebido = valorTributoRecebido;
	}

	@Column(name = "valor_desconto_recebido", nullable = false)
	public BigDecimal getValorDescontoRecebido() {
		return valorDescontoRecebido;
	}

	public void setValorDescontoRecebido(BigDecimal valorDescontoRecebido) {
		this.valorDescontoRecebido = valorDescontoRecebido;
	}

	@Column(name = "valor_devolucao_recebido", nullable = false)
	public BigDecimal getValorDevolucaoRecebido() {
		return valorDevolucaoRecebido;
	}

	public void setValorDevolucaoRecebido(BigDecimal valorDevolucaoRecebido) {
		this.valorDevolucaoRecebido = valorDevolucaoRecebido;
	}

	@Column(name = "valor_liquido_recebido", nullable = false)
	public BigDecimal getValorLiquidoRecebido() {
		return valorLiquidoRecebido;
	}

	public void setValorLiquidoRecebido(BigDecimal valorLiquidoRecebido) {
		this.valorLiquidoRecebido = valorLiquidoRecebido;
	}

	@Column(name = "tipo_documento_previsto", nullable = false)
	public Pagamento.TipoDocumentoPagamento getTipoDocumentoPrevisto() {
		return tipoDocumentoPrevisto;
	}

	public void setTipoDocumentoPrevisto(Pagamento.TipoDocumentoPagamento tipoDocumentoPrevisto) {
		this.tipoDocumentoPrevisto = tipoDocumentoPrevisto;
	}

	@Column(name = "tipo_documento_recebimento")
	public Pagamento.TipoDocumentoPagamento getTipoDocumentoRecebimento() {
		return tipoDocumentoRecebimento;
	}

	public void setTipoDocumentoRecebimento(Pagamento.TipoDocumentoPagamento tipoDocumentoRecebimento) {
		this.tipoDocumentoRecebimento = tipoDocumentoRecebimento;
	}

	@Column(name = "protocolo_entrega")
	public String getProtocoloEntrega() {
		return protocoloEntrega;
	}

	public void setProtocoloEntrega(String protocoloEntrega) {
		this.protocoloEntrega = protocoloEntrega;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_resp_numeracao", nullable = false)
	public Date getDataResponsavelNumeracao() {
		return dataResponsavelNumeracao;
	}

	public void setDataResponsavelNumeracao(Date dataResponsavelNumeracao) {
		this.dataResponsavelNumeracao = dataResponsavelNumeracao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_resp_canc_numeracao", nullable = false)
	public Date getDataResponsavelCancelarNumeracao() {
		return dataResponsavelCancelarNumeracao;
	}

	public void setDataResponsavelCancelarNumeracao(Date dataResponsavelCancelarNumeracao) {
		this.dataResponsavelCancelarNumeracao = dataResponsavelCancelarNumeracao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_resp_recebimento", nullable = false)
	public Date getDataResponsavelRecebimento() {
		return dataResponsavelRecebimento;
	}

	public void setDataResponsavelRecebimento(Date dataResponsavelRecebimento) {
		this.dataResponsavelRecebimento = dataResponsavelRecebimento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_resp_canc_recebimento", nullable = false)
	public Date getDataResponsavelCancelarRecebimento() {
		return dataResponsavelCancelarRecebimento;
	}

	public void setDataResponsavelCancelarRecebimento(Date dataResponsavelCancelarRecebimento) {
		this.dataResponsavelCancelarRecebimento = dataResponsavelCancelarRecebimento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_resp_cancelamento", nullable = false)
	public Date getDataResponsavelCancelamento() {
		return dataResponsavelCancelamento;
	}

	public void setDataResponsavelCancelamento(Date dataResponsavelCancelamento) {
		this.dataResponsavelCancelamento = dataResponsavelCancelamento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_resp_reversao_cancelamento", nullable = false)
	public Date getDataResponsavelReverterCancelamento() {
		return dataResponsavelReverterCancelamento;
	}

	public void setDataResponsavelReverterCancelamento(Date dataResponsavelReverterCancelamento) {
		this.dataResponsavelReverterCancelamento = dataResponsavelReverterCancelamento;
	}

	@OneToMany(mappedBy = "notaDebito")
	public List<FaturamentoEtapa> getFaturamentoEtapas() {
		return faturamentoEtapas;
	}

	public void setFaturamentoEtapas(List<FaturamentoEtapa> faturamentoEtapas) {
		this.faturamentoEtapas = faturamentoEtapas;
	}

	@OneToMany(mappedBy = "notaDebito", orphanRemoval = true)
	public List<MotivoRetornoDocumentoNotaDebito> getMotivosRetornoDocumentoNotaDebito() {
		return motivosRetornoDocumentoNotaDebito;
	}

	public void setMotivosRetornoDocumentoNotaDebito(List<MotivoRetornoDocumentoNotaDebito> motivosRetornoDocumentoNotaDebito) {
		this.motivosRetornoDocumentoNotaDebito = motivosRetornoDocumentoNotaDebito;
	}

	@Transient
	public int getEtapaNumero() {
		return etapaNumero;
	}

	public void setEtapaNumero(int etapaNumero) {
		this.etapaNumero = etapaNumero;
	}

	@Transient
	public String getEtapaDescricao() {
		return etapaDescricao;
	}

	public void setEtapaDescricao(String etapaDescricao) {
		this.etapaDescricao = etapaDescricao;
	}

	public enum SituacaoNotaDebito {
		ABERTA("Aberta"),
		EM_RECEBIMENTO("Em recebimento"),
		RECEBIDA("Recebida"),
		CANCELADA("Cancelada");

		private final String descricao;

		SituacaoNotaDebito(String descricao) {
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

	@Override
	public String toString() {
		return "NotaDebito{" +
			"empresa=" + empresa.getCodnome() +
			", centroFinanceiro=" + centroFinanceiro.getIdentificador() +
			", cliente=" + cliente.getDocumento() +
			", servicoTipo=" + servicoTipo +
			", cidade=" + cidade.getNome() +
			", numero='" + numero +
			", situacao=" + situacao.getDescricao() +
			", dataEmissao=" + dataEmissao +
			", dataVencimento=" + dataVencimento +
			", dataPagamento=" + dataPagamento +
			", dataAgendamento=" + dataAgendamento +
			", dataContabilizacao=" + dataContabilizacao +
			", descricao='" + descricao +
			", valorBruto=" + valorBruto +
			", valorTibutoPrevisto=" + valorTibutoPrevisto +
			", valorDescontoPrevisto=" + valorDescontoPrevisto +
			", valorDevolucaoPrevisto=" + valorDevolucaoPrevisto +
			", valorLiquidoPrevisto=" + valorLiquidoPrevisto +
			", valorMulta=" + valorMulta +
			", valorJuros=" + valorJuros +
			", valorTributoRecebido=" + valorTributoRecebido +
			", valorDescontoRecebido=" + valorDescontoRecebido +
			", valorDevolucaoRecebido=" + valorDevolucaoRecebido +
			", valorLiquidoRecebido=" + valorLiquidoRecebido +
			", tipoDocumentoPrevisto=" + tipoDocumentoPrevisto +
			", tipoDocumentoRecebimento=" + tipoDocumentoRecebimento +
			'}';
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

		if (!(obj instanceof NotaDebito)) {
			return false;
		}

		NotaDebito other = (NotaDebito) obj;

		if (getCodigo() == null) {
			return other.getCodigo() == null;
		} else return getCodigo().equals(other.getCodigo());

	}
}

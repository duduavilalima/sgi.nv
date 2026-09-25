package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Relação Bancária: representa a relação de pagamentos que será enviada para o banco
 * efetuar o pagamento. Uma relação bancária só pode agrupar pagamentos com datas
 * de vencimento iguais.
 *
 * @author Rodrigo Barreiros
 * @see ItemRelacao
 */
@Entity
@Table(name = "RELACAO_BANCARIA")
@SequenceGenerator(name = "SEQ_RELACAO_BANCARIA", sequenceName = "SEQ_RELACAO_BANCARIA", allocationSize = 1)
public class RelacaoBancaria extends AuditEntity implements DomainObject {

	public static final String QUERY_FIND_BY_STATUS = "RelacaoBancaria.findByStatus";
	public static final String QUERY_FIND_BY_DATA_PAGAMENTO_AND_FINALIZADO = "RelacaoBancaria.findByDataPagamentoAndStatus";
	public static final String QUERY_FIND_BY_DATA_PAGAMENTO_AND_FINALIZADO_AND_EMPRESA = "RelacaoBancaria.findByDataPagamentoAndStatusAndEmpresa";

	private static final long serialVersionUID = 5352445080426276908L;

	private ContaCorrente contaCorrente;
	private Unidade unidade;
	private Colaborador colaboradorEnvio;
	private Colaborador colaboradorBaixaManual;
	private Colaborador colaboradorEstornoBaixaManual;
	private Colaborador colaboradorCancelamento;

	private String numero;
	private Date dataPagamento;
	private MeioPagamento meioPagamento;
	private Long chequeAnterior;
	private Boolean pagamentoIndividual;
	private BigDecimal total;
	private Integer qtdPagamentos;
	private String observacao;
	private StatusRelacaoBancaria status;
	private Date dataEnvio;
	private Date dataBaixaManual;
	private Date dataEstornoBaixaManual;
	private Date dataCancelamento;
	private Boolean impressaoCheque = Boolean.FALSE;
	private Boolean impressaoEndosso = Boolean.FALSE;
	private Boolean impressao2via = Boolean.FALSE;
	private Date dataEnvioIb;
	private Date dataRetornoIb;
	private String motivoBaixaManual;

	private List<ItemRelacao> itens = new ArrayList<>();


	/**
	 * Informa se a relação bancária é um pagamento avulso.<br />
	 * <code>true</code> é um pagamento avulso. <code>false</code> não é um pagamento avulso.
	 */
	private Boolean pagamentoAvulso;
	/**
	 * Informa se a relação e de origem do Movimento Autorizado.
	 */
	private boolean relacaoDeMovAutorizado;

	/**
	 * Adiciona um novo Item à lista de itens da relação.
	 *
	 * @param itemRelacao o novo item
	 */
	public void addItemRelacao(ItemRelacao itemRelacao) {
		itemRelacao.setRelacaoBancaria(this);
		itens.add(itemRelacao);
	}

	@Override
	public void setCodigo(Long codigo) {
		this.numero = "RB/" + codigo;
		super.setCodigo(codigo);
	}

	@Column(name = "NUMERO")
	@Length(max = 14)
	@NotNull
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "DATA_PAGAMENTO", nullable = false)
	@Temporal(TemporalType.DATE)
	@NotNull
	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	@Column(name = "TOTAL", nullable = false)
	@NotNull
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Column(name = "CHEQUE_ANTERIOR")
	public Long getChequeAnterior() {
		return chequeAnterior;
	}

	public void setChequeAnterior(Long chequeAnterior) {
		this.chequeAnterior = chequeAnterior;
	}

	@Column(name = "MEIO_PAGAMENTO")
	@NotNull
	public MeioPagamento getMeioPagamento() {
		return meioPagamento;
	}

	public void setMeioPagamento(MeioPagamento meioPagamento) {
		this.meioPagamento = meioPagamento;
	}

	@Column(name = "OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "relacaoBancaria")
	@OrderBy(value = "codigo")
	public List<ItemRelacao> getItens() {
		return itens;
	}

	public void setItens(List<ItemRelacao> itens) {
		this.itens = itens;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CORRENTE")
	@NotNull
	public ContaCorrente getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrente contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_UNIDADE")
	@NotNull
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ENVIO")
	public Colaborador getColaboradorEnvio() {
		return colaboradorEnvio;
	}

	public void setColaboradorEnvio(Colaborador colaboradorEnvio) {
		this.colaboradorEnvio = colaboradorEnvio;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_BAIXA_MANUAL")
	public Colaborador getColaboradorBaixaManual() {
		return colaboradorBaixaManual;
	}

	public void setColaboradorBaixaManual(Colaborador colaboradorBaixaManual) {
		this.colaboradorBaixaManual = colaboradorBaixaManual;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ESTORNO_BAIXA_MANUAL")
	public Colaborador getColaboradorEstornoBaixaManual() {
		return colaboradorEstornoBaixaManual;
	}

	public void setColaboradorEstornoBaixaManual(Colaborador colaboradorEstornoBaixaManual) {
		this.colaboradorEstornoBaixaManual = colaboradorEstornoBaixaManual;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CANCELAMENTO")
	public Colaborador getColaboradorCancelamento() {
		return colaboradorCancelamento;
	}

	public void setColaboradorCancelamento(Colaborador colaboradorCancelamento) {
		this.colaboradorCancelamento = colaboradorCancelamento;
	}

	@Column(name = "DATA_ENVIO")
	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	@Column(name = "DATA_BAIXA_MANUAL")
	public Date getDataBaixaManual() {
		return dataBaixaManual;
	}

	public void setDataBaixaManual(Date dataBaixaManual) {
		this.dataBaixaManual = dataBaixaManual;
	}

	@Column(name = "DATA_ESTORNO_BAIXA_MANUAL")
	public Date getDataEstornoBaixaManual() {
		return dataEstornoBaixaManual;
	}

	public void setDataEstornoBaixaManual(Date dataEstornoBaixaManual) {
		this.dataEstornoBaixaManual = dataEstornoBaixaManual;
	}

	@Column(name = "DATA_CANCELAMENTO")
	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	@Column(name = "PAGAMENTO_INDIVIDUAL")
	public Boolean getPagamentoIndividual() {
		return pagamentoIndividual;
	}

	public void setPagamentoIndividual(Boolean pagamentoIndividual) {
		this.pagamentoIndividual = pagamentoIndividual;
	}

	@Column(name = "impressao_cheque")
	public Boolean getImpressaoCheque() {
		return impressaoCheque;
	}

	public void setImpressaoCheque(Boolean impressaoCheque) {
		this.impressaoCheque = impressaoCheque;
	}

	@Column(name = "impressao_endosso")
	public Boolean getImpressaoEndosso() {
		return impressaoEndosso;
	}

	public void setImpressaoEndosso(Boolean impressaoEndosso) {
		this.impressaoEndosso = impressaoEndosso;
	}

	@Column(name = "impressao_2_via")
	public Boolean getImpressao2via() {
		return impressao2via;
	}

	public void setImpressao2via(Boolean impressao2ia) {
		this.impressao2via = impressao2ia;
	}

	@Column(name = "PAGAMENTOS")
	public Integer getQtdPagamentos() {
		return qtdPagamentos;
	}

	public void setQtdPagamentos(Integer qtdPagamentos) {
		this.qtdPagamentos = qtdPagamentos;
	}

	/**
	 * Obter o total de pagamentos.
	 *
	 * @return O total de pagamento.
	 */
	public int obterTotalDePagamento() {
		if (getItens().isEmpty()) {
			return 0;
		} else {
			int value = 0;
			for (ItemRelacao itemRelacao : getItens()) {
				value += itemRelacao.getPagamentos().size();
			}
			return value;
		}
	}

	@Column(name = "SITUACAO")
	@NotNull
	public StatusRelacaoBancaria getStatus() {
		return status;
	}

	public void setStatus(StatusRelacaoBancaria status) {
		this.status = status;
	}

	@Column(name = "DATA_ENVIO_IB")
	public Date getDataEnvioIb() {
		return dataEnvioIb;
	}

	public void setDataEnvioIb(Date dataEnvioIb) {
		this.dataEnvioIb = dataEnvioIb;
	}

	@Column(name = "DATA_RETORNO_IB")
	public Date getDataRetornoIb() {
		return dataRetornoIb;
	}

	public void setDataRetornoIb(Date dataRetornoIb) {
		this.dataRetornoIb = dataRetornoIb;
	}

	@Column(name = "MOTIVO_BAIXA_MANUAL")
	public String getMotivoBaixaManual() {
		return motivoBaixaManual;
	}

	public void setMotivoBaixaManual(String motivoBaixaManual) {
		this.motivoBaixaManual = motivoBaixaManual;
	}

	/**
	 * Indica se o pagamento da relação foi confirmado (baixa bancária).
	 * <p>Para que a relação seja confirmado, todos os itens devem ter
	 * sido confirmados.
	 *
	 * @return true, se confirmado, false, caso contrário
	 */
	@Transient
	public boolean isConfirmado() {
		for (ItemRelacao itemRelacao : itens) {
			if (!itemRelacao.isConfirmado()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Verifica se o status da {@link RelacaoBancaria} está em aguardando retorno.
	 *
	 * @return <code>true</code> caso status de "Aguardando retorno". <code>false</code> caso contrário.
	 */
	@Transient
	public boolean isAguardandoRetorno() {
		return StatusRelacaoBancaria.AGUARDANDO_RETORNO.equals(getStatus());
	}

	/**
	 * Verifica se o status da {@link RelacaoBancaria} está aberta.
	 *
	 * @return <code>true</code> caso status de "Aberta". <code>false</code> caso contrário.
	 * @see StatusRelacaoBancaria
	 */
	@Transient
	public boolean isAberta() {
		return StatusRelacaoBancaria.ABERTA.equals(getStatus());
	}

	/**
	 * Verifica se o status da {@link RelacaoBancaria} está finalizada.
	 *
	 * @return <code>true</code> caso status de "finalizada". <code>false</code> caso contrário.
	 */
	@Transient
	public boolean isFinalizada() {
		return StatusRelacaoBancaria.FINALIZADA.equals(getStatus());
	}

	/**
	 * Verifica se o status da {@link RelacaoBancaria} está cancelada.
	 *
	 * @return <code>true</code> caso status de "cancelada". <code>false</code> caso contrário.
	 */
	@Transient
	public boolean isCancelada() {
		return StatusRelacaoBancaria.CANCELADA.equals(getStatus());
	}

	/**
	 * Verifica se o {@link MeioPagamento} da {@link RelacaoBancaria} é do
	 * tipo "CHEQUE".
	 *
	 * @return <code>true</code> caso {@link MeioPagamento} for do tipo CHEQUE.
	 * <code>false</code> caso contrário.
	 */
	@Transient
	public boolean isCheque() {
		return MeioPagamento.CHEQUE.equals(getMeioPagamento());
	}

	/**
	 * Informa se a relação bancária é um pagamento avulso.
	 *
	 * @return <code>true</code> é um pagamento avulso.
	 * <code>false</code> não é um pagamento avulso.
	 */
	@Transient
	public Boolean getPagamentoAvulso() {
		return pagamentoAvulso;
	}

	/**
	 * Informa se a relação bancária é um pagamento avulso.
	 *
	 * @param pagamentoAvulso
	 * @return <code>true</code> é um pagamento avulso.
	 * <code>false</code> não é um pagamento avulso.
	 */
	public void setPagamentoAvulso(Boolean pagamentoAvulso) {
		this.pagamentoAvulso = pagamentoAvulso;
	}

	/**
	 * Informa se a relação bancária é de origem do Movimento Autorizado.
	 *
	 * @return <code>true</code> origem movimento autorizado. <code>false</code> caso contrário.
	 */
	@Transient
	public boolean getRelacaoDeMovAutorizado() {
		return relacaoDeMovAutorizado;
	}

	/**
	 * Ajustar se a relação bancária é de origem do Movimento Autorizado.
	 *
	 * @return <code>true</code> origem movimento autorizado. <code>false</code> caso contrário.
	 */
	public void setRelacaoDeMovAutorizado(boolean relacaoDeMovAutorizado) {
		this.relacaoDeMovAutorizado = relacaoDeMovAutorizado;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		RelacaoBancaria that = (RelacaoBancaria) o;
		return relacaoDeMovAutorizado == that.relacaoDeMovAutorizado &&
			Objects.equals(numero, that.numero) &&
			Objects.equals(dataPagamento, that.dataPagamento) &&
			Objects.equals(total, that.total) &&
			Objects.equals(chequeAnterior, that.chequeAnterior) &&
			Objects.equals(observacao, that.observacao) &&
			Objects.equals(dataEnvio, that.dataEnvio) &&
			Objects.equals(dataCancelamento, that.dataCancelamento) &&
			Objects.equals(pagamentoIndividual, that.pagamentoIndividual) &&
			Objects.equals(impressaoCheque, that.impressaoCheque) &&
			Objects.equals(impressaoEndosso, that.impressaoEndosso) &&
			Objects.equals(impressao2via, that.impressao2via) &&
			Objects.equals(qtdPagamentos, that.qtdPagamentos) &&
			Objects.equals(dataEnvioIb, that.dataEnvioIb) &&
			Objects.equals(dataRetornoIb, that.dataRetornoIb) &&
			Objects.equals(motivoBaixaManual, that.motivoBaixaManual) &&
			Objects.equals(pagamentoAvulso, that.pagamentoAvulso);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), numero, dataPagamento, total, chequeAnterior, observacao, dataEnvio, dataCancelamento, pagamentoIndividual, impressaoCheque, impressaoEndosso, impressao2via, qtdPagamentos, dataEnvioIb, dataRetornoIb, pagamentoAvulso, relacaoDeMovAutorizado);
	}

	@Override
	public String toString() {
		return "RelacaoBancaria [codigo=" + getCodigo() + ", numero=" + numero + "]";
	}

	/**
	 * Os tipos de meio de pagamento de uma relação bancária.
	 */
	public enum MeioPagamento {

		INTERNET_BANKING("Internet banking"),
		ORDEM_CREDITO("Ordem de crédito"),
		CHEQUE("Cheque"),
		BANKLINE_MANUAL("Bankline manual");

		private final String descricao;

		private MeioPagamento(String descricao) {
			this.descricao = descricao;
		}

		public static MeioPagamento findByDescricao(String descricao) {
			for (MeioPagamento meioPagamento : values()) {
				if (meioPagamento.descricao.equals(descricao)) return meioPagamento;
			}
			return null;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return getDescricao();
		}
	}

	/**
	 * O status possíveis para a {@link RelacaoBancaria}.
	 */
	public enum StatusRelacaoBancaria {
		ABERTA("Aberta"),
		EM_PROCESSAMENTO("Em processamento (internet banking)"),
		AGUARDANDO_RETORNO("Aguardando retorno"),
		PROCESSANDO_RETORNO("Processando retorno (internet banking)"),
		FINALIZADA("Finalizada"),
		CANCELADA("Cancelada"),
		A_PROCESSAR("A processar (internet banking)");

		private final String descricao;

		StatusRelacaoBancaria(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}


		@Override
		public String toString() {
			return this.descricao;
		}
	}

}


package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.TipoDocumentoPagamento;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.DadosBancarios;
import br.com.fasolti.corporativo.pessoa.domain.DadosBancarios.TipoConta;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Agrupa os pagamentos de um único fornecedor que possuem a mesma data de vencimento.
 *
 * @author Rodrigo Barreiros
 * @see RelacaoBancaria
 */
@Entity
@Table(name = "ITEM_RELACAO")
@SequenceGenerator(name = "SEQ_ITEM_RELACAO", sequenceName = "SEQ_ITEM_RELACAO", allocationSize = 1)
public class ItemRelacao extends BaseEntity implements Comparable<ItemRelacao>, DomainObject {

	private static final long serialVersionUID = 7041044296613086095L;

	private StatusItemRelacao status;
	private boolean selecionado = true;
	private boolean pagamentoSemNumeroBoleto = false;
	private boolean vencimentoPosterior = false;
	private boolean notaFiscalComTributoErrado = false;
	private RelacaoBancaria relacaoBancaria;
	private List<Pagamento> pagamentos = new ArrayList<>();
	private Long cheque;
	private BigDecimal total;
	private Integer qtdPagamentos;
	private String caminhoComprovante;
	private RetornoBancario retornoBancario;
	private String controle;
	private Date dataEfetivacaoPagamento;
	private Banco banco;
	private String agencia;
	private String digitoAgencia;
	private String conta;
	private String digito;
	private TipoContaBancaria tipoConta;
	private TipoDocumentoPagamento tipoDocumentoPagamento;
	private Date dataPagamento;
	private boolean pgtoAgrupadoCodigoBarras;
	private Integer ordemParcelaNF;
	private boolean erroValorLinhaDigitavel = false;
	private List<ItemRelacaoPagamento> listaPagamentos;
	private BigDecimal valorAPagar = BigDecimal.ZERO;

	public ItemRelacao() {
	}

	public ItemRelacao(Long codigo) {
		setCodigo(codigo);
	}

	public void addPagamento(Pagamento pagamento) {
		pagamentos.add(pagamento);
	}

	/**
	 * Obter o total do valor de todos os pagamento.
	 * São levados em consideração os juros e multas.
	 *
	 * @return O valor total de todos os pagamento.
	 */
	@Transient
	public BigDecimal getTotalPagamentos() {
		BigDecimal totalPg = new BigDecimal(0);
		for (Pagamento pagamento : pagamentos) {
			totalPg = totalPg.add(pagamento.getValorPagar());
		}
		return totalPg;
	}

	/**
	 * Obter o total do valor de todos os pagamento por tipo de documento.
	 * São levados em consideração os juros e multas.
	 *
	 * @return O valor total de todos os pagamento.
	 */
	@Transient
	public BigDecimal getTotalPagamentosPorTipoDoc(TipoDocumentoPagamento tipoDoc) {
		BigDecimal totalPg = new BigDecimal(0);
		boolean existeTipoDoc = false;
		for (Pagamento pagamento : pagamentos) {
			if (pagamento.getTipoDocumentoPagamento() != null && pagamento.getTipoDocumentoPagamento().equals(tipoDoc)) {
				existeTipoDoc = true;
				totalPg = totalPg.add(pagamento.getValorPagar());
			}
		}
		if (!existeTipoDoc)
			return null;
		return totalPg;
	}

	@Transient
	public boolean isConfirmado() {
		if (status != null) {
			return status.equals(StatusItemRelacao.CONFIRMADO);
		}
		return false;
	}

	@Column(name = "SITUACAO")
	@NotNull
	public StatusItemRelacao getStatus() {
		return status;
	}

	public void setStatus(StatusItemRelacao status) {
		this.status = status;
	}

	/**
	 * Usado para controle dos itens selecionados na camanda de apresentação.
	 *
	 * @return true, se estiver selecionado, false, caso contrário
	 */
	@Transient
	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	@Transient
	public boolean isVencimentoPosterior() {
		return vencimentoPosterior;
	}

	public void setVencimentoPosterior(boolean vencimentoPosterior) {
		this.vencimentoPosterior = vencimentoPosterior;
	}

	@Transient
	public boolean isPagamentoSemNumeroBoleto() {
		return pagamentoSemNumeroBoleto;
	}

	public void setPagamentoSemNumeroBoleto(boolean pagamentoSemNumeroBoleto) {
		this.pagamentoSemNumeroBoleto = pagamentoSemNumeroBoleto;
	}

	@Transient
	public boolean isNotaFiscalComTributoErrado() {
		return notaFiscalComTributoErrado;
	}

	public void setNotaFiscalComTributoErrado(boolean notaFiscalComTributoErrado) {
		this.notaFiscalComTributoErrado = notaFiscalComTributoErrado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RELACAO_BANCARIA")
	public RelacaoBancaria getRelacaoBancaria() {
		return relacaoBancaria;
	}

	public void setRelacaoBancaria(RelacaoBancaria relacaoBancaria) {
		this.relacaoBancaria = relacaoBancaria;
	}

	//    @ManyToMany(fetch = FetchType.LAZY)
//	@JoinTable(name = "ITEM_RELACAO_PAGAMENTO",
//		joinColumns =
//				{@JoinColumn(name = "CODIGO_ITEM_RELACAO", updatable = false, insertable = false) }, inverseJoinColumns = { @JoinColumn(name = "CODIGO_PAGAMENTO", updatable = false, insertable = false) }
//			)
//    @OrderBy(value = "codigo")
	@Transient
	public List<Pagamento> getPagamentos() {
//        pagamentos = new ArrayList<Pagamento>();
//        for (ItemRelacaoPagamento irp : this.getListaPagamentos()) {
//            Pagamento pgtoAux = irp.getPagamento();
//            if (pgtoAux instanceof HibernateProxy) {
//                Object pgto = pgtoAux;
//                pgtoAux = (Pagamento) ((HibernateProxy) pgto).getHibernateLazyInitializer().getImplementation();
//            }
//            pagamentos.add(pgtoAux);
//        }

		return pagamentos;
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	@Column(name = "CHEQUE")
	public Long getCheque() {
		return cheque;
	}

	public void setCheque(Long cheque) {
		this.cheque = cheque;
	}

	@Column(name = "TOTAL", nullable = false)
	@NotNull
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Column(name = "PAGAMENTOS")
	public Integer getQtdPagamentos() {
		return qtdPagamentos;
	}

	public void setQtdPagamentos(Integer qtdPagamentos) {
		this.qtdPagamentos = qtdPagamentos;
	}

	@Column(name = "CAMINHO_COMPROVANTE")
	public String getCaminhoComprovante() {
		return caminhoComprovante;
	}

	public void setCaminhoComprovante(String caminhoComprovante) {
		this.caminhoComprovante = caminhoComprovante;
	}

	@Column(name = "CONTROLE")
	public String getControle() {
		return controle;
	}

	public void setControle(String controle) {
		this.controle = controle;
	}

	@Column(name = "EFETIVACAO_PAGAMENTO")
	@Temporal(TemporalType.DATE)
	public Date getDataEfetivacaoPagamento() {
		return dataEfetivacaoPagamento;
	}

	public void setDataEfetivacaoPagamento(Date dataEfetivacaoPagamento) {
		this.dataEfetivacaoPagamento = dataEfetivacaoPagamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RETORNO_BANCARIO")
	public RetornoBancario getRetornoBancario() {
		return retornoBancario;
	}

	public void setRetornoBancario(RetornoBancario retornoBancario) {
		this.retornoBancario = retornoBancario;
	}

	@Transient
	public Pessoa getCredor() {
		return !pagamentos.isEmpty() ? pagamentos.get(0).getCredor() : null;
	}

	@Transient
	public Integer getOrdemParcelaNF() {
		return ordemParcelaNF;
	}

	public void setOrdemParcelaNF(Integer ordemParcelaNF) {
		this.ordemParcelaNF = ordemParcelaNF;
	}

	@OneToMany(mappedBy = "itemRelacao", fetch = FetchType.LAZY, orphanRemoval = true)
	public List<ItemRelacaoPagamento> getListaPagamentos() {
		return listaPagamentos;
	}

	public void setListaPagamentos(List<ItemRelacaoPagamento> listaPagamentos) {
		this.listaPagamentos = listaPagamentos;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_BANCO")
	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	@Column(name = "AGENCIA")
	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	@Column(name = "DIGITO_AGENCIA")
	public String getDigitoAgencia() {
		return digitoAgencia;
	}

	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	@Column(name = "CONTA")
	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	@Column(name = "DIGITO_CONTA")
	public String getDigito() {
		return digito;
	}

	public void setDigito(String digito) {
		this.digito = digito;
	}

	@Column(name = "TIPO_CONTA")
	public TipoContaBancaria getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(TipoContaBancaria tipoConta) {
		this.tipoConta = tipoConta;
	}

	@Column(name = "TIPO_DOC_PAGAMENTO")
	public TipoDocumentoPagamento getTipoDocumentoPagamento() {
		return tipoDocumentoPagamento;
	}

	public void setTipoDocumentoPagamento(
		TipoDocumentoPagamento tipoDocumentoPagamento) {
		this.tipoDocumentoPagamento = tipoDocumentoPagamento;
	}

	@Column(name = "DATA_PAGAMENTO")
	@Temporal(TemporalType.DATE)
	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	@Transient
	public boolean existemDadosBancariosRB() {
		if (banco != null && banco.getCodigo() != null
			&& agencia != null && !agencia.isEmpty()
			&& conta != null && !conta.isEmpty()
			&& digito != null && !digito.isEmpty()
			&& tipoConta != null
			&& tipoDocumentoPagamento != null
			&& dataPagamento != null)
			return true;
		return false;
	}

	@Transient
	public boolean isPgtoAgrupadoCodigoBarras() {
		return pgtoAgrupadoCodigoBarras;
	}

	public void setPgtoAgrupadoCodigoBarras(boolean pgtoAgrupadoCodigoBarras) {
		this.pgtoAgrupadoCodigoBarras = pgtoAgrupadoCodigoBarras;
	}

	@Transient
	public boolean isErroValorLinhaDigitavel() {
		return erroValorLinhaDigitavel;
	}

	public void setErroValorLinhaDigitavel(boolean erroValorLinhaDigitavel) {
		this.erroValorLinhaDigitavel = erroValorLinhaDigitavel;
	}

	@Transient
	public List<DadosBancarios> getDadosBancariosCongelados() {
		List<DadosBancarios> dados = new ArrayList<>();
		DadosBancarios bancarios = new DadosBancarios();
		bancarios.setBanco(this.getBanco());
		bancarios.setAgencia(this.getAgencia());
		bancarios.setDigitoAgencia(this.getDigitoAgencia());
		bancarios.setConta(this.getConta());
		bancarios.setDigito(this.getDigito());
		if (TipoContaBancaria.CONTA_CORRENTE.equals(this.getTipoConta()))
			bancarios.setTipoConta(TipoConta.CONTA_CORRENTE);
		if (TipoContaBancaria.CONTA_POUPANCA.equals(this.getTipoConta()))
			bancarios.setTipoConta(TipoConta.POUPANCA);
		dados.add(bancarios);
		return dados;
	}

	@Transient
	public BigDecimal getValorAPagar() {
		return valorAPagar;
	}

	public void setValorAPagar(BigDecimal valorAPagar) {
		this.valorAPagar = valorAPagar;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), selecionado, pagamentoSemNumeroBoleto, vencimentoPosterior, notaFiscalComTributoErrado, cheque, total, qtdPagamentos, caminhoComprovante, controle, dataEfetivacaoPagamento, agencia, digitoAgencia, conta, digito, tipoConta, tipoDocumentoPagamento, dataPagamento, pgtoAgrupadoCodigoBarras, ordemParcelaNF, erroValorLinhaDigitavel, valorAPagar);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		if (!(obj instanceof ItemRelacao)) {
			return false;
		}

		ItemRelacao other = (ItemRelacao) obj;

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
		return "ItemRelacao [codigo=" + getCodigo() + ", status=" + status + ", total=" + total + ", qtdPagamentos=" + qtdPagamentos + "]";
	}

	@Override
	public int compareTo(ItemRelacao o) {
		return o.getCheque() == null ? 0 : getCheque().compareTo(o.getCheque());
	}

	public enum StatusItemRelacao {
		ABERTO("Aberto"),
		ENVIADO("Enviado à instituição"),
		AGUARDANDO_APROVACAO("Aguardando aprovação"),
		APROVADO("Aprovado"),
		ESTORNADO("Inválido/Estornado"),
		CONFIRMADO("Pago");

		private final String nome;

		StatusItemRelacao(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return nome;
		}

	}

	public static enum TipoContaBancaria {
		CONTA_CORRENTE("Conta corrente"),
		CONTA_POUPANCA("Conta poupança"),
		CONTA_SALARIO("Conta salário");

		private final String nome;

		private TipoContaBancaria(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return nome;
		}

	}

}

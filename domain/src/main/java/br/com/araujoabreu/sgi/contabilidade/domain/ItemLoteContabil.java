package br.com.araujoabreu.sgi.contabilidade.domain;

import br.com.araujoabreu.sgi.administracao.domain.Diretoria;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.tesouraria.domain.Lancamento;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "ITEM_LOTE_CONTABIL")
@SequenceGenerator(name = "SEQ_ITEM_LOTE_CONTABIL", sequenceName = "SEQ_ITEM_LOTE_CONTABIL", allocationSize = 1)
public class ItemLoteContabil extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5701297961225428368L;
	private ContaContabil contaContabil;
	private HistoricoPadraoContabil historicoPadrao;
	private String complemento;
	private BigDecimal valor;
	private String debitoCredito;
	private Pagamento pagamento;
	private Lancamento lancamento;
	private String numeroContaContabil;
	private LoteInterface loteInterface;
	private String identificadorContaContabil;
	private String identificadorNumeroContaContabil;
	private Diretoria diretoria;
	private Date dataPagamento;
	private Boolean diferenca;
	private String fluxoCaixa;
	private String numeroFormatado;

	public ItemLoteContabil(ContaContabil contaContabil,
							HistoricoPadraoContabil historicoPadrao,
							String complemento,
							BigDecimal valor,
							String debitoCredito,
							Pagamento pagamento,
							String numeroContaContabil,
							LoteInterface loteInterface,
							String identificadorContaContabil,
							String identificadorNumeroContaContabil,
							Diretoria diretoria,
							Date dataPagamento) {
		super();
		this.contaContabil = contaContabil;
		this.historicoPadrao = historicoPadrao;
		this.complemento = complemento;
		this.valor = valor;
		this.debitoCredito = debitoCredito;
		this.pagamento = pagamento;
		this.numeroContaContabil = numeroContaContabil;
		this.loteInterface = loteInterface;
		this.identificadorContaContabil = identificadorContaContabil;
		this.identificadorNumeroContaContabil = identificadorNumeroContaContabil;
		this.diretoria = diretoria;
		this.dataPagamento = dataPagamento;
	}

	public ItemLoteContabil() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL")
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_HISTORICO_PADRAO_CONTABIL")
	public HistoricoPadraoContabil getHistoricoPadrao() {
		return historicoPadrao;
	}

	public void setHistoricoPadrao(HistoricoPadraoContabil historicoPadrao) {
		this.historicoPadrao = historicoPadrao;
	}

	@Column(name = "COMPLEMENTO")
	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@Column(name = "VALOR")
	@NotNull
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Transient
	public BigDecimal getValorFormatado() {
		return valor.abs();
	}

	@Column(name = "DEBITO_CREDITO", length = 1)
	@NotNull
	public String getDebitoCredito() {
		return debitoCredito;
	}

	public void setDebitoCredito(String debitoCredito) {
		this.debitoCredito = debitoCredito;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PAGAMENTO")
	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_LANCAMENTO")
	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	@Column(name = "NUMERO_CONTA_CONTABIL")
	public String getNumeroContaContabil() {
		return numeroContaContabil;
	}

	public void setNumeroContaContabil(String numeroContaContabil) {
		this.numeroContaContabil = numeroContaContabil;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_LOTE_INTERFACE", nullable = false)
	public LoteInterface getLoteInterface() {
		return loteInterface;
	}

	public void setLoteInterface(LoteInterface loteInterface) {
		this.loteInterface = loteInterface;
	}

	@Column(name = "DIFERENCA")
	public Boolean getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(Boolean diferenca) {
		this.diferenca = diferenca;
	}

	@Transient
	public String getIdentificadorContaContabil() {
		return identificadorContaContabil;
	}

	public void setIdentificadorContaContabil(String identificadorContaContabil) {
		this.identificadorContaContabil = identificadorContaContabil;
	}

	@Transient
	public String getIdentificadorNumeroContaContabil() {
		return identificadorNumeroContaContabil;
	}

	public void setIdentificadorNumeroContaContabil(String identificadorNumeroContaContabil) {
		this.identificadorNumeroContaContabil = identificadorNumeroContaContabil;
	}

	@Transient
	public Diretoria getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(Diretoria diretoria) {
		this.diretoria = diretoria;
	}

	@Transient
	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	//Utilizado apenas para a geração do lote contábil de fluxo de caixa.
	@Transient
	public String getFluxoCaixa() {
		return fluxoCaixa;
	}

	public void setFluxoCaixa(String fluxoCaixa) {
		this.fluxoCaixa = fluxoCaixa;
	}

	//	Utilizado apenas para a geração do lote contábil de fluxo de caixa.
	@Transient
	public String getNumeroFormatado() {
		return numeroFormatado;
	}

	public void setNumeroFormatado(String numeroFormatado) {
		this.numeroFormatado = numeroFormatado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((complemento == null) ? 0 : complemento.hashCode());
		result = prime * result + ((debitoCredito == null) ? 0 : debitoCredito.hashCode());
		result = prime * result + ((numeroContaContabil == null) ? 0 : numeroContaContabil.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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

		if (!(obj instanceof ItemLoteContabil)) {
			return false;
		}

		ItemLoteContabil other = (ItemLoteContabil) obj;
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
		return "ItemLoteContabil [codigo=" + getCodigo() + ", complemento=" + complemento + ", valor=" + valor + ", debitoCredito=" + debitoCredito + "]";
	}

}

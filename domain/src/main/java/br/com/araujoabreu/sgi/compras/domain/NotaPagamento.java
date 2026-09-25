package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.Status;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.TipoPagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.RateioNotaPagamento;
import br.com.araujoabreu.sgi.fundoFixo.domain.Demonstrativo;
import br.com.araujoabreu.sgi.fundoFixo.domain.FundoFixo;
import br.com.araujoabreu.sgi.fundoFixo.enums.TipoConta;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.Length;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NOTA_PAGAMENTO")
public class NotaPagamento extends Nota {

	private static final long serialVersionUID = -4515622574089471295L;
	private String processo;
	private FundoFixo fundoFixo;
	private NotaFiscal notaFiscal;
	private List<RateioNotaPagamento> rateios;
	private List<Demonstrativo> demonstrativos;
	private BigDecimal valor = new BigDecimal(0);
	private BigDecimal valorJuros = new BigDecimal(0);
	private BigDecimal valorMulta = new BigDecimal(0);
	private BigDecimal valorTributo = new BigDecimal(0);
	private BigDecimal saldoAdiantamento = new BigDecimal(0);
	private ContaContabil contaContabil;
	private boolean creditoFundoFixo;
	private final StringBuffer logImportacao = new StringBuffer();
	private BigDecimal valorOutrosDescontos = new BigDecimal(0);

	private List<Pagamento> listaAdiantamentos;

	public NotaPagamento() {
		setCredor(new Pessoa());
		setTipo(TipoPagamento.NOTA_PAGAMENTO);
	}

	@Transient
	public boolean isProcessoJudicial() {
		if (getNaturezaPagamento() != null) {
			return getNaturezaPagamento().getCodigo().equals(2L); //TODO: Codigo hard-codificdo!
		}
		return false;
	}

	@Transient
	public boolean isCreditoFundoFixo() {
		return creditoFundoFixo;
	}

	public void setCreditoFundoFixo(boolean creditoFundoFixo) {
		this.creditoFundoFixo = creditoFundoFixo;
	}

	@Override
	public void setCodigo(Long codigo) {
		super.setCodigo(codigo);
		setNumero(getTipo().getDescricao() + "/" + getCodigo());
	}

	@Column(name = "PROCESSO", length = 20)
	@Length(max = 20)
	public String getProcesso() {
		return this.processo;
	}

	public void setProcesso(String cfop) {
		this.processo = cfop;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_FUNDO_FIXO")
	public FundoFixo getFundoFixo() {
		return fundoFixo;
	}

	public void setFundoFixo(FundoFixo fundoFixo) {
		this.fundoFixo = fundoFixo;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "CODIGO_NOTA_FISCAL")
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "notaPagamento")
	@OrderBy(value = "codigo")
	public List<Demonstrativo> getDemonstrativos() {
		return demonstrativos;
	}

	public void setDemonstrativos(List<Demonstrativo> demonstrativos) {
		this.demonstrativos = demonstrativos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "notaPagamento")
	public List<RateioNotaPagamento> getRateios() {
		return rateios;
	}

	public void setRateios(List<RateioNotaPagamento> rateios) {
		this.rateios = rateios;
	}

	@Column(name = "VALOR_JUROS", precision = 9, scale = 2)
	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	@Column(name = "VALOR_MULTA", precision = 9, scale = 2)
	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	@Column(name = "SALDO_ADIANTAMENTO", precision = 9, scale = 2)
	public BigDecimal getSaldoAdiantamento() {
		return saldoAdiantamento;
	}

	public void setSaldoAdiantamento(BigDecimal saldoAdiantamento) {
		this.saldoAdiantamento = saldoAdiantamento;
	}

	@Column(name = "VALOR_TRIBUTO", precision = 9, scale = 2)
	public BigDecimal getValorTributo() {
		return valorTributo;
	}

	public void setValorTributo(BigDecimal valorTributo) {
		this.valorTributo = valorTributo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL")
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "npAdiantamento")
	@OrderBy(value = "codigo")
	public List<Pagamento> getListaAdiantamentos() {
		return listaAdiantamentos;
	}

	public void setListaAdiantamentos(List<Pagamento> listaAdiantamentos) {
		this.listaAdiantamentos = listaAdiantamentos;
	}

	@Transient
	public BigDecimal getValorPagar() {
		if (this.isParcelaUnica())
			valor = this.getValor();
		else
			valor = this.getParcelas().get(0).getValor();
		if (valorJuros != null && !valorJuros.equals(BigDecimal.ZERO)) {
			this.valor = this.valor.add(valorJuros);
		}
		if (valorMulta != null && !valorMulta.equals(BigDecimal.ZERO)) {
			this.valor = this.valor.add(valorMulta);
		}

		if (valorTributo != null && !valorTributo.equals(BigDecimal.ZERO)) {
			this.valor = this.valor.subtract(valorTributo);
		}

		if (getValorDesconto() != null && !getValorDesconto().equals(BigDecimal.ZERO)) {
			this.valor = this.valor.subtract(getValorDesconto());
		}

		if (getTotalDescontos() != null && !getTotalDescontos().equals(BigDecimal.ZERO)) {
			this.valor = this.valor.subtract(getTotalDescontos());
		}

		return valor;
	}

	@Transient
	public String getNotaAdiantamentoFormatado() {
		StringBuilder sb = new StringBuilder();
		if (getNumero() != null) {
			sb.append(getNumero());
		}

		if (saldoAdiantamento != null) {
			DecimalFormat formatador = new DecimalFormat("###,###,###,###.00");
			sb.append(" - R$ ").append(formatador.format(saldoAdiantamento));
		}
		if (getDataVencimento() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sb.append(" - Venc: ").append(sdf.format(getDataVencimento()));
		}

		return sb.toString();
	}

	@Transient
	public String getValorNotaAdiantamentoFormatado() {
		StringBuilder sb = new StringBuilder();
		if (getNumero() != null) {
			sb.append(getNumero());
		}

		if (valor != null) {
			DecimalFormat formatador = new DecimalFormat("###,###,###,###.00");
			sb.append(" - R$ ").append(formatador.format(super.getValor()));
		}
		if (getDataVencimento() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sb.append(" - Venc: ").append(sdf.format(getDataVencimento()));
		}

		return sb.toString();
	}

	@Transient
	public List<RateioNotaPagamento> getRateiosAgrupados() {
		List<RateioNotaPagamento> rateiosAgrupados = new ArrayList<>();
		if (rateios != null && !rateios.isEmpty()) {
			List<RateioNotaPagamento> rateios = new ArrayList<>();
			rateios.addAll(this.rateios);
			List<Rateio> rateiosAux = super.getRateiosAgrupado(new ArrayList<>(rateios));
			for (Rateio rateioAux : rateiosAux) {
				rateiosAgrupados.add((RateioNotaPagamento) rateioAux);
			}
		}
		return rateiosAgrupados;
	}

	@Transient
	public TipoConta getTipoConta() {
		if (demonstrativos != null && !demonstrativos.isEmpty()) {
			return demonstrativos.get(0).getTipo();
		}
		return null;
	}

	@Transient
	public Status getSituacaoPagamento() {
		if (isParcelaUnica() && getParcelas() != null && !getParcelas().isEmpty()) {
			return getParcelas().get(0).getStatus();
		}
		return null;
	}

	@Transient
	public PessoaJuridica getFornecedor() {
		return (PessoaJuridica) getCredor();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
			+ ((processo == null) ? 0 : processo.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result
			+ ((valorJuros == null) ? 0 : valorJuros.hashCode());
		result = prime * result
			+ ((valorMulta == null) ? 0 : valorMulta.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof NotaPagamento)) {
			return false;
		}
		NotaPagamento other = (NotaPagamento) obj;
		if (processo == null) {
			if (other.getProcesso() != null) {
				return false;
			}
		} else if (!processo.equals(other.getProcesso())) {
			return false;
		}
		if (valor == null) {
			if (other.getValor() != null) {
				return false;
			}
		} else if (!valor.equals(other.getValor())) {
			return false;
		}
		if (valorJuros == null) {
			if (other.getValorJuros() != null) {
				return false;
			}
		} else if (!valorJuros.equals(other.getValorJuros())) {
			return false;
		}
		if (valorMulta == null) {
			if (other.getValorMulta() != null) {
				return false;
			}
		} else if (!valorMulta.equals(other.getValorMulta())) {
			return false;
		}
		return true;
	}

	@Transient
	public StringBuffer getLogImportacao() {
		return logImportacao;
	}

	@Transient
	public void addLogImportacao(String log) {
		logImportacao.append("• ").append(log).append("<br/>");
	}

	@Transient
	public String getLogImportacaoFormatado() {
		if (logImportacao == null)
			return null;

		if (StringUtils.isEmpty(logImportacao.toString()))
			return null;

		return logImportacao.toString();
	}

	@Override
	public String toString() {
		return "NotaPagamento [codigo=" + getCodigo()
			+ ", valor=" + valor + ", valorJuros=" + valorJuros + ", valorMulta=" + valorMulta + ", valorTributo=" + valorTributo
			+ ", saldoAdiantamento=" + saldoAdiantamento
			+ ", creditoFundoFixo=" + creditoFundoFixo + ", logImportacao=" + logImportacao + "]";
	}

	@Transient
	public BigDecimal[] getValoresNF(Pagamento pagamento) {
		BigDecimal[] valores = new BigDecimal[13];

		//Valor da nota
		valores[2] = tratarValores(getValor());

		//Multa (+)
		valores[0] = tratarValores(getValorMulta());

		//Valor da 1º parcela
		if (getParcelas() != null && !getParcelas().isEmpty()) {
			valores[5] = tratarValores(getParcelas().get(0).getValor());
		} else {
			valores[5] = tratarValores(BigDecimal.ZERO);
		}

		//Juros (+)
		valores[1] = tratarValores(getValorJuros()); //0,00

		//Tributos (-)
		valores[3] = tratarValores(getValorTributo()); //0,00

		//Desconto (-)
		valores[6] = tratarValores(getValorDesconto()); //-2000,00

		//Outros desc. (-)
		valores[7] = tratarValores(getTotalDescontos()); //-608,29

		//A pagar (=)
		valores[4] = tratarValores(getValorPagar()); //-2608,29 ??

		return valores; //valor 1a parcela =9608,29
	}

	@Transient
	private BigDecimal tratarValores(BigDecimal valor) {
		if (valor != null) {
			return valor.setScale(2, RoundingMode.HALF_UP);
		} else {
			return new BigDecimal("0.00");
		}
	}

	@Transient
	public BigDecimal getValorOutrosDescontos() {
		return valorOutrosDescontos;
	}

	public void setValorOutrosDescontos(BigDecimal valorOutrosDescontos) {
		this.valorOutrosDescontos = valorOutrosDescontos;
	}

}

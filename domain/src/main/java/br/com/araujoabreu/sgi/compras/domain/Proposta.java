package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PROPOSTA")
@SequenceGenerator(name="SEQ_PROPOSTA", sequenceName="SEQ_PROPOSTA", allocationSize=1)
public class Proposta extends AuditEntity implements Detail, Comparable<Proposta> {

	private static final long serialVersionUID = -8036661446992551112L;
	private String numero;
	private OrdemCompra ordemCompra;
	private Pessoa fornecedor;
	private List<ItemProposta> itens = new ArrayList<>(0);
	private String condicaoPagamento;
	private Integer quantidadePrazo;
	private TipoPrazo tipoPrazo;
	private Date dataEntrega;
	private BigDecimal valorDesconto = new BigDecimal(0);
	private BigDecimal percentualDesconto = new BigDecimal(0);
	private TipoDesconto tipoDesconto = TipoDesconto.VALOR;
	private BigDecimal freteSeguro = new BigDecimal(0);
	private BigDecimal valorAVista = new BigDecimal(0);
	private String observacao;
	private Boolean selecionado = Boolean.FALSE;
	private String condicaoPagamentoNegociada;
	private Integer quantidadePrazoNegociada;
	private TipoPrazo tipoPrazoNegociado;
	private Date dataEntregaNegociada;
	private BigDecimal valorDescontoNegociado;
	private BigDecimal percentualDescontoNegociado;
	private TipoDesconto tipoDescontoNegociado;
	private BigDecimal freteSeguroNegociado;
	private BigDecimal valorAVistaNegociado;
	private String observacaoNegociada;

	public Proposta() {

	}

	public Proposta(Pessoa fornecedor) {
		this.fornecedor = fornecedor;
	}

	public void addItem(ItemProposta itemProposta) {
		itemProposta.setProposta(this);
		itens.add(itemProposta);
	}

	@Override
	public void setCodigo(Long codigo) {
		this.numero = "CO/" + codigo;
		super.setCodigo(codigo);
	}

	@Column(name = "NUMERO")
	@NotNull
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ORDEM_COMPRA")
	@NotNull
	public OrdemCompra getOrdemCompra() {
		return ordemCompra;
	}

	public void setOrdemCompra(OrdemCompra ordemCompra) {
		this.ordemCompra = ordemCompra;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_FORNECEDOR")
	@NotNull
	public Pessoa getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Pessoa fornecedor) {
		this.fornecedor = fornecedor;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "proposta")
	@OrderBy(value = "descricao")
	public List<ItemProposta> getItens() {
		return itens;
	}

	public void setItens(List<ItemProposta> itens) {
		this.itens = itens;
	}

	@Column(name = "CONDICAO_PAGAMENTO")
	public String getCondicaoPagamento() {
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(String condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	@Column(name = "QUANTIDADE_PRAZO")
	public Integer getQuantidadePrazo() {
		return quantidadePrazo;
	}

	public void setQuantidadePrazo(Integer quantidadePrazo) {
		this.quantidadePrazo = quantidadePrazo;
	}

	@Column(name = "TIPO_PRAZO")
	public TipoPrazo getTipoPrazo() {
		return tipoPrazo;
	}

	public void setTipoPrazo(TipoPrazo tipoPrazo) {
		this.tipoPrazo = tipoPrazo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_ENTREGA")
	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	@Column(name = "VALOR_DESCONTO", precision = 15)
	@NotNull
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	@Column(name = "PERCENTUAL_DESCONTO", precision = 4)
	@NotNull
	public BigDecimal getPercentualDesconto() {
		return percentualDesconto;
	}

	public void setPercentualDesconto(BigDecimal percentualDesconto) {
		this.percentualDesconto = percentualDesconto;
	}

	@Column(name = "TIPO_DESCONTO")
	@NotNull
	public TipoDesconto getTipoDesconto() {
		return tipoDesconto;
	}

	public void setTipoDesconto(TipoDesconto tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	@Column(name = "FRETE_SEGURO", precision = 15)
	@NotNull
	public BigDecimal getFreteSeguro() {
		return freteSeguro;
	}

	public void setFreteSeguro(BigDecimal freteSeguro) {
		this.freteSeguro = freteSeguro;
	}

	@Column(name = "VALOR_A_VISTA", precision = 15)
	@NotNull
	public BigDecimal getValorAVista() {
		return valorAVista;
	}

	public void setValorAVista(BigDecimal valorAVista) {
		this.valorAVista = valorAVista;
	}

	@Column(name = "OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Column(name = "SELECIONADO")
	@NotNull
	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	@Column(name = "CONDICAO_PAGAMENTO_NEGOCIADA")
	public String getCondicaoPagamentoNegociada() {
		return condicaoPagamentoNegociada;
	}

	public void setCondicaoPagamentoNegociada(String condicaoPagamentoNegociada) {
		this.condicaoPagamentoNegociada = condicaoPagamentoNegociada;
	}

	@Column(name = "QUANTIDADE_PRAZO_NEGOCIADA")
	public Integer getQuantidadePrazoNegociada() {
		return quantidadePrazoNegociada;
	}

	public void setQuantidadePrazoNegociada(Integer quantidadePrazoNegociada) {
		this.quantidadePrazoNegociada = quantidadePrazoNegociada;
	}

	@Column(name = "TIPO_PRAZO_NEGOCIADO")
	public TipoPrazo getTipoPrazoNegociado() {
		return tipoPrazoNegociado;
	}

	public void setTipoPrazoNegociado(TipoPrazo tipoPrazoNegociado) {
		this.tipoPrazoNegociado = tipoPrazoNegociado;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_ENTREGA_NEGOCIADA")
	public Date getDataEntregaNegociada() {
		return dataEntregaNegociada;
	}

	public void setDataEntregaNegociada(Date dataEntregaNegociada) {
		this.dataEntregaNegociada = dataEntregaNegociada;
	}

	@Column(name = "VALOR_DESCONTO_NEGOCIADO", precision = 15)
	public BigDecimal getValorDescontoNegociado() {
		return valorDescontoNegociado;
	}

	public void setValorDescontoNegociado(BigDecimal valorDescontoNegociado) {
		this.valorDescontoNegociado = valorDescontoNegociado;
	}

	@Column(name = "PERCENTUAL_DESCONTO_NEGOCIADO", precision = 4)
	public BigDecimal getPercentualDescontoNegociado() {
		return percentualDescontoNegociado;
	}

	public void setPercentualDescontoNegociado(BigDecimal percentualDescontoNegociado) {
		this.percentualDescontoNegociado = percentualDescontoNegociado;
	}

	@Column(name = "TIPO_DESCONTO_NEGOCIADO")
	public TipoDesconto getTipoDescontoNegociado() {
		return tipoDescontoNegociado;
	}

	public void setTipoDescontoNegociado(TipoDesconto tipoDescontoNegociado) {
		this.tipoDescontoNegociado = tipoDescontoNegociado;
	}

	@Column(name = "FRETE_SEGURO_NEGOCIADO", precision = 15)
	public BigDecimal getFreteSeguroNegociado() {
		return freteSeguroNegociado;
	}

	public void setFreteSeguroNegociado(BigDecimal freteSeguroNegociado) {
		this.freteSeguroNegociado = freteSeguroNegociado;
	}

	@Column(name = "VALOR_A_VISTA_NEGOCIADO", precision = 15)
	public BigDecimal getValorAVistaNegociado() {
		return valorAVistaNegociado;
	}

	public void setValorAVistaNegociado(BigDecimal valorAVistaNegociado) {
		this.valorAVistaNegociado = valorAVistaNegociado;
	}

	@Column(name = "OBSERVACAO_NEGOCIADA")
	public String getObservacaoNegociada() {
		return observacaoNegociada;
	}

	public void setObservacaoNegociada(String observacaoNegociada) {
		this.observacaoNegociada = observacaoNegociada;
	}

	@Override
	@Transient
	public Boolean getPrincipal() {
		return false;
	}

	@Override
	public void setPrincipal(Boolean principal) {
	}

	/**
	 * Obter o valor final com desconto e frete incluso.
	 *
	 * @return O valor final com desconto e frete incluso.
	 */
	@Transient
	public BigDecimal getValorFinalComFrete() {
		return getTotal().subtract(getValorDesconto()).add(getFreteSeguro());
	}

	@Transient
	public BigDecimal getValorFinalComFreteNegociado() {
		if (valorDescontoNegociado == null) {
			valorDescontoNegociado = BigDecimal.valueOf(valorDesconto.doubleValue());
		}
		if (freteSeguroNegociado == null) {
			freteSeguroNegociado = BigDecimal.valueOf(freteSeguro.doubleValue());
		}
		return getTotalNegociado().subtract(valorDescontoNegociado).add(freteSeguroNegociado);
	}

	/**
	 * Obter o valor final com desconto e sem o valor do frete.
	 *
	 * @return O valor final com desconto e sem o valor do frete.
	 */
	@Transient
	public BigDecimal getValorFinalSemFrete() {
		return getTotal().subtract(getValorDesconto());
	}

	@Transient
	public BigDecimal getValorFinalSemFreteNegociado() {
		if (valorDescontoNegociado == null) {
			valorDescontoNegociado = BigDecimal.valueOf(valorDesconto.doubleValue());
		}
		return getTotalNegociado().subtract(valorDescontoNegociado);
	}

	/**
	 * Retorna a soma dos valores de cada item da proposta.
	 *
	 * @return o valor total da proposta
	 */
	@Transient
	public BigDecimal getTotal() {
		BigDecimal total = new BigDecimal(0);
		for (ItemProposta item : itens) {
			if (item.getValor() != null) {
				if (item.getValor() != null) {
					total = total.add(item.getValor());
				}
			}
		}
		return total;
	}

	@Transient
	public BigDecimal getTotalComDesconto() {
		BigDecimal total = new BigDecimal(0);
		for (ItemProposta item : itens) {
			if (item.getValor() != null) {
				if (item.getValor() != null) {
					total = total.add(item.getValor());
				}
			}
		}
		return total.subtract(valorDesconto);
	}

	@Transient
	public BigDecimal getTotalNegociado() {
		BigDecimal total = new BigDecimal(0);
		for (ItemProposta item : itens) {
			if (item.getValorNegociado() != null) {
				if (item.getValorNegociado() != null) {
					total = total.add(item.getValorNegociado());
				}
			}
		}
		return total;
	}

	/**
	 * Itens informados: quantidade de itens preenchidos na cotação.
	 *
	 * @return Quantidade de itens preenchidos na cotação.
	 */
	@Transient
	public int getQuantidadeItensPreenchidos() {
		int total = 0;
		for (ItemProposta item : itens) {
			if (item.getValor() != null && item.getQuantidadeInformada() != null) {
				if (item.getValor().doubleValue() > 0d && item.getQuantidadeInformada().intValue() > 0) {
					total += 1;
				}
			}
		}
		return total;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((condicaoPagamento == null) ? 0 : condicaoPagamento.hashCode());
		result = prime * result + ((condicaoPagamentoNegociada == null) ? 0 : condicaoPagamentoNegociada.hashCode());
		result = prime * result + ((dataEntrega == null) ? 0 : dataEntrega.hashCode());
		result = prime * result + ((freteSeguro == null) ? 0 : freteSeguro.hashCode());
		result = prime * result + ((freteSeguroNegociado == null) ? 0 : freteSeguroNegociado.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((observacao == null) ? 0 : observacao.hashCode());
		result = prime * result + ((observacaoNegociada == null) ? 0 : observacaoNegociada.hashCode());
		result = prime * result + ((percentualDesconto == null) ? 0 : percentualDesconto.hashCode());
		result = prime * result + ((percentualDescontoNegociado == null) ? 0 : percentualDescontoNegociado.hashCode());
		result = prime * result + ((quantidadePrazo == null) ? 0 : quantidadePrazo.hashCode());
		result = prime * result + ((quantidadePrazoNegociada == null) ? 0 : quantidadePrazoNegociada.hashCode());
		result = prime * result + ((tipoDesconto == null) ? 0 : tipoDesconto.hashCode());
		result = prime * result + ((tipoDescontoNegociado == null) ? 0 : tipoDescontoNegociado.hashCode());
		result = prime * result + ((tipoPrazo == null) ? 0 : tipoPrazo.hashCode());
		result = prime * result + ((tipoPrazoNegociado == null) ? 0 : tipoPrazoNegociado.hashCode());
		result = prime * result + ((valorAVista == null) ? 0 : valorAVista.hashCode());
		result = prime * result + ((valorAVistaNegociado == null) ? 0 : valorAVistaNegociado.hashCode());
		result = prime * result + ((valorDesconto == null) ? 0 : valorDesconto.hashCode());
		result = prime * result + ((valorDescontoNegociado == null) ? 0 : valorDescontoNegociado.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(Proposta.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Proposta other = (Proposta) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (condicaoPagamento == null) {
			if (other.getCondicaoPagamento() != null) {
				return false;
			}
		} else if (!condicaoPagamento.equals(other.getCondicaoPagamento())) {
			return false;
		}
		if (condicaoPagamentoNegociada == null) {
			if (other.getCondicaoPagamentoNegociada() != null) {
				return false;
			}
		} else if (!condicaoPagamentoNegociada.equals(other.getCondicaoPagamentoNegociada())) {
			return false;
		}
		if (dataEntrega == null) {
			if (other.getDataEntrega() != null) {
				return false;
			}
		} else if (!dataEntrega.equals(other.getDataEntrega())) {
			return false;
		}
		if (freteSeguro == null) {
			if (other.getFreteSeguro() != null) {
				return false;
			}
		} else if (!freteSeguro.equals(other.getFreteSeguro())) {
			return false;
		}
		if (freteSeguroNegociado == null) {
			if (other.getFreteSeguroNegociado() != null) {
				return false;
			}
		} else if (!freteSeguroNegociado.equals(other.getFreteSeguroNegociado())) {
			return false;
		}
		if (numero == null) {
			if (other.getNumero() != null) {
				return false;
			}
		} else if (!numero.equals(other.getNumero())) {
			return false;
		}
		if (observacao == null) {
			if (other.getObservacao() != null) {
				return false;
			}
		} else if (!observacao.equals(other.getObservacao())) {
			return false;
		}
		if (observacaoNegociada == null) {
			if (other.getObservacaoNegociada() != null) {
				return false;
			}
		} else if (!observacaoNegociada.equals(other.getObservacaoNegociada())) {
			return false;
		}
		if (percentualDesconto == null) {
			if (other.getPercentualDesconto() != null) {
				return false;
			}
		} else if (!percentualDesconto.equals(other.getPercentualDesconto())) {
			return false;
		}
		if (percentualDescontoNegociado == null) {
			if (other.getPercentualDescontoNegociado() != null) {
				return false;
			}
		} else if (!percentualDescontoNegociado.equals(other.getPercentualDescontoNegociado())) {
			return false;
		}
		if (quantidadePrazo == null) {
			if (other.getQuantidadePrazo() != null) {
				return false;
			}
		} else if (!quantidadePrazo.equals(other.getQuantidadePrazo())) {
			return false;
		}
		if (quantidadePrazoNegociada == null) {
			if (other.getQuantidadePrazoNegociada() != null) {
				return false;
			}
		} else if (!quantidadePrazoNegociada.equals(other.getQuantidadePrazoNegociada())) {
			return false;
		}
		if (tipoDesconto != other.getTipoDesconto()) {
			return false;
		}
		if (tipoDescontoNegociado != other.getTipoDescontoNegociado()) {
			return false;
		}
		if (tipoPrazo != other.getTipoPrazo()) {
			return false;
		}
		if (tipoPrazoNegociado != other.getTipoPrazoNegociado()) {
			return false;
		}
		if (valorAVista == null) {
			if (other.getValorAVista() != null) {
				return false;
			}
		} else if (!valorAVista.equals(other.getValorAVista())) {
			return false;
		}
		if (valorAVistaNegociado == null) {
			if (other.getValorAVistaNegociado() != null) {
				return false;
			}
		} else if (!valorAVistaNegociado.equals(other.getValorAVistaNegociado())) {
			return false;
		}
		if (valorDesconto == null) {
			if (other.getValorDesconto() != null) {
				return false;
			}
		} else if (!valorDesconto.equals(other.getValorDesconto())) {
			return false;
		}
		if (valorDescontoNegociado == null) {
			if (other.getValorDescontoNegociado() != null) {
				return false;
			}
		} else if (!valorDescontoNegociado.equals(other.getValorDescontoNegociado())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Proposta [codigo=" + getCodigo() + ", numero=" + numero + ", fornecedor=" + fornecedor + ", quantidadePrazo=" + quantidadePrazo + "]";
	}

	@Override
	public int compareTo(Proposta o) {
		return (o.getTotal().compareTo(this.getTotal())) * -1;
	}

	public enum TipoPrazo {
		DIAS("dias"),
		DIAS_UTEIS("dias úteis");

		private final String nome;

		TipoPrazo(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}
	}

	public enum TipoDesconto {
		VALOR("valor"),
		PERCENTUAL("percentual");

		private final String nome;

		TipoDesconto(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}
	}

}

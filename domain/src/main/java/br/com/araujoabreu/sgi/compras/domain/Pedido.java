package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.compras.domain.Proposta.TipoDesconto;
import br.com.araujoabreu.sgi.compras.domain.Proposta.TipoPrazo;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Representa o Pedido que será enviado para compra. Agrupa os Itens selecionados como de
 * melhor preço (avaliação objetiva) ou melhor custo/benefício
 * (avaliação subjetiva).
 * <p>
 * É o Pedido que foi autorizado e que será enviado para aquisição.
 *
 * @author Rodrigo Barreiros
 * @since 1.0
 */
@Entity
@Table(name = "PEDIDO")
@SequenceGenerator(name="SEQ_PEDIDO", sequenceName="SEQ_PEDIDO", allocationSize=1)
public class Pedido extends AuditEntity {

	private static final long serialVersionUID = -4383845943214142756L;
	private Status status;
	private OrdemCompra ordemCompra;
	private Pessoa fornecedor;
	private List<Recebimento> recebimentos = new ArrayList<>(0);
	private List<ItemPedido> itens = new ArrayList<>(0);
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

	public Pedido() {

	}

	public Pedido(Pessoa fornecedor) {
		this.fornecedor = fornecedor;
	}

	public void addItem(ItemPedido itemPedido) {
		itemPedido.setPedido(this);
		itens.add(itemPedido);
	}

	public void addRecebimento(Recebimento recebimento) {
		recebimento.setPedido(this);
		recebimentos.add(recebimento);
	}

	@Column(name = "STATUS")
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pedido")
	@OrderBy(value = "codigo")
	public List<Recebimento> getRecebimentos() {
		return recebimentos;
	}

	public void setRecebimentos(List<Recebimento> recebimentos) {
		this.recebimentos = recebimentos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pedido")
	public List<ItemPedido> getItens() {
		if (itens != null && !itens.isEmpty()) {
			Comparator<ItemPedido> comparator = (o1, o2) -> o1.getItemProposta().getDescricao().compareToIgnoreCase(o2.getItemProposta().getDescricao());

			itens.sort(comparator);
		}

		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
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

	@Column(name = "VALOR_DESCONTO", precision = 15, scale = 2)
	@NotNull
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	@Column(name = "PERCENTUAL_DESCONTO", precision = 4, scale = 2)
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

	@Column(name = "FRETE_SEGURO", precision = 15, scale = 2)
	@NotNull
	public BigDecimal getFreteSeguro() {
		return freteSeguro;
	}

	public void setFreteSeguro(BigDecimal freteSeguro) {
		this.freteSeguro = freteSeguro;
	}

	@Column(name = "VALOR_A_VISTA", precision = 15, scale = 2)
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

	/**
	 * Retorna a soma dos valores de cada recebimento.
	 *
	 * @return
	 */
	@Transient
	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		if (recebimentos.isEmpty()) {
			return getTotalSolicitado();
		} else {
			for (Recebimento item : recebimentos) {
				total = total.add(item.getTotal());
			}
		}
		return total;
	}

	/**
	 * Retorna a soma dos valores (negociados) de cada item da proposta.
	 *
	 * @return o valor total do pedido
	 */
	@Transient
	public BigDecimal getTotalSolicitado() {
		BigDecimal total = BigDecimal.ZERO;
		for (ItemPedido itemPedido : itens) {
			if (itemPedido.getItemProposta().getValor() != null) {
				if (itemPedido.getItemProposta() != null) {
					if (itemPedido.getItemProposta().getValorNegociado() != null) {
						total = total.add(itemPedido.getItemProposta().getValorNegociado());
					}
				}
			}
		}
		return total;
	}

	/**
	 * Retorna a soma dos valores (não negociados) de cada item da proposta.
	 *
	 * @return o valor total do pedido
	 */
	@Transient
	public BigDecimal getTotalSolicitadoNaoNegociado() {
		BigDecimal total = BigDecimal.ZERO;
		for (ItemPedido itemPedido : itens) {
			if (itemPedido.getItemProposta().getValor() != null) {
				if (itemPedido.getItemProposta() != null) {
					if (itemPedido.getItemProposta().getValor() != null) {
						total = total.add(itemPedido.getItemProposta().getValor());
					}
				}
			}
		}
		return total;
	}

	/**
	 * Obter o valor final com desconto e frete incluso.
	 *
	 * @return O valor final com desconto e frete incluso.
	 */
	@Transient
	public BigDecimal getValorFinalComFrete() {
		return getTotal().subtract(valorDesconto).add(freteSeguro);
	}

	/**
	 * Obter o valor final com desconto e sem o valor do frete.
	 *
	 * @return O valor final com desconto e sem o valor do frete.
	 */
	@Transient
	public BigDecimal getValorFinalSemFrete() {
		return getTotal().subtract(valorDesconto);
	}

	/**
	 * Itens informados: quantidade de itens preenchidos na cotação.
	 *
	 * @return Quantidade de itens preenchidos na cotação.
	 */
	@Transient
	public Integer getQuantidadeItensPreenchidos() {
		int total = 0;
		for (ItemPedido item : itens) {
			if (item.getValor() != null && item.getQuantidade() != null) {
				if (item.getValor().doubleValue() > 0d && item.getQuantidade().intValue() > 0) {
					total += 1;
				}
			}
		}
		return total;
	}

	@Transient
	public boolean isAberto() {
		return Status.ABERTO.equals(this.status);
	}

	@Transient
	public boolean isParcial() {
		return Status.PARCIAL.equals(this.status);
	}

	@Transient
	public boolean isTotal() {
		return Status.TOTAL.equals(this.status);
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
		if (!(Pedido.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Pedido other = (Pedido) obj;
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
		return "Pedido [codigo=" + getCodigo() + ", status=" + status + ", ordemCompra=" + ordemCompra + "]";
	}

	/**
	 * Indicador do status de recebimento do Pedido
	 **/
	public enum Status {ABERTO, PARCIAL, TOTAL}

}

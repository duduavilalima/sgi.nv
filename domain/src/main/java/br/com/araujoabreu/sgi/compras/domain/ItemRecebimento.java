package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.fasolti.framework.domain.BaseEntity;
import br.com.fasolti.tools.FormatarBigDecimal;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Representa o item confirmado no momento do recebimento.
 * Determina a quantidade recebida do item e qual foi o preço unitário praticado no recebimento.
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "ITEM_RECEBIMENTO")
@SequenceGenerator(name="SEQ_ITEM_RECEBIMENTO", sequenceName="SEQ_ITEM_RECEBIMENTO", allocationSize=1)
public class ItemRecebimento extends BaseEntity {

	private static final long serialVersionUID = 6232050705543220708L;
	private BigDecimal quantidade;
	private BigDecimal preco;
	private ItemPedido itemPedido;
	private Recebimento recebimento;
	private ContaContabil contaContabilCentroLucro;
	private ContaContabil contaContabilCentroCusto;
	private String descricaoContaContabilCentroLucro;
	private String descricaoContaContabilCentroCusto;

	private List<RateioItemRecebimento> rateios = new ArrayList<>();

	@Column(name = "QUANTIDADE", precision = 9, scale = 2)
	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	@Transient
	public String getQuantidade4Decimais() {
		if (getItemPedido() != null && getItemPedido().getItemProposta().getItemCompra().getUnidadeMedida() != null)
			quantidade = quantidade.setScale(4, RoundingMode.HALF_EVEN);

		return FormatarBigDecimal.formatBigDecimal(quantidade);
	}

	@Transient
	public void setQuantidade4Decimais(String quantidadeStr) {
		this.quantidade = FormatarBigDecimal.parseBigDecimal(quantidadeStr);
	}

	@Transient
	public String getQuantidadeStr() {
		if (getItemPedido() != null && getItemPedido().getItemProposta().getItemCompra().getUnidadeMedida() != null)
			quantidade = quantidade.setScale(getItemPedido().getItemProposta().getItemCompra().getUnidadeMedida().getNumCasasDecimais(), RoundingMode.HALF_EVEN);

		return FormatarBigDecimal.formatBigDecimal(quantidade);
	}

	@Transient
	public void setQuantidadeStr(String quantidadeStr) {
		this.quantidade = FormatarBigDecimal.parseBigDecimal(quantidadeStr);
	}

	@Column(name = "PRECO", precision = 11, scale = 2)
	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ITEM_PEDIDO")
	@NotNull
	public ItemPedido getItemPedido() {
		return itemPedido;
	}

	public void setItemPedido(ItemPedido item) {
		this.itemPedido = item;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RECEBIMENTO")
	@NotNull
	public Recebimento getRecebimento() {
		return recebimento;
	}

	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}

	@Transient
	public BigDecimal getValor() {
		if (preco != null && quantidade != null) {
			return getPreco().multiply(quantidade);
		}
		return null;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL_CENTRO_LUCRO")
	public ContaContabil getContaContabilCentroLucro() {
		return contaContabilCentroLucro;
	}

	public void setContaContabilCentroLucro(ContaContabil contaContabilCentroLucro) {
		this.contaContabilCentroLucro = contaContabilCentroLucro;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL_CENTRO_CUSTO")
	public ContaContabil getContaContabilCentroCusto() {
		return contaContabilCentroCusto;
	}

	public void setContaContabilCentroCusto(ContaContabil contaContabilCentroCusto) {
		this.contaContabilCentroCusto = contaContabilCentroCusto;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "itemRecebimento")
	public List<RateioItemRecebimento> getRateios() {
		return rateios;
	}

	public void setRateios(List<RateioItemRecebimento> rateios) {
		this.rateios = rateios;
	}

	@Transient
	public String getDescricaoContaContabilCentroLucro() {
		return descricaoContaContabilCentroLucro;
	}

	public void setDescricaoContaContabilCentroLucro(String descricaoContaContabilCentroLucro) {
		this.descricaoContaContabilCentroLucro = descricaoContaContabilCentroLucro;
	}

	@Transient
	public boolean isContaContabilCentroCustoPermitido() {
		CentroFinanceiro centroFinanceiro = getDescricaoCentroFinanceiro();
		return (centroFinanceiro != null && centroFinanceiro.getPai() == null);
	}

	@Transient
	public boolean isContaContabilCentroLucroPermitido() {
		CentroFinanceiro centroFinanceiro = getDescricaoCentroFinanceiro();
		return (centroFinanceiro != null && centroFinanceiro.getPai() != null);
	}

	@Transient
	public String getDescricaoContaContabilCentroCusto() {
		return descricaoContaContabilCentroCusto;
	}

	public void setDescricaoContaContabilCentroCusto(String descricaoContaContabilCentroCusto) {
		this.descricaoContaContabilCentroCusto = descricaoContaContabilCentroCusto;
	}

	@Transient
	public String getPrecoStr() {
		preco = (preco != null) ? preco.setScale(4, RoundingMode.HALF_EVEN) : preco;
		return FormatarBigDecimal.formatBigDecimal(preco);
	}

	@Transient
	public void setPrecoStr(String quantidadeStr) {
		this.preco = FormatarBigDecimal.parseBigDecimal(quantidadeStr);
	}

	@Transient
	public CentroFinanceiro getDescricaoCentroFinanceiro() {
		CentroFinanceiro[] centrosFinanceiros = getCentrosFinanceiroNotaFiscalOC();
		CentroFinanceiro centroFinanceiro = new CentroFinanceiro();
		if (centrosFinanceiros != null) {
			centroFinanceiro = centrosFinanceiros[0];
		}
		return centroFinanceiro;
	}

	@Transient
	private CentroFinanceiro[] getCentrosFinanceiroNotaFiscalOC() {
		LinkedHashSet<CentroFinanceiro> centros = new LinkedHashSet<>();
		Pedido pedido = this.getRecebimento().getPedido();
		List<RequisicaoCompraRefinada> reqRefinadas = pedido.getOrdemCompra().getRequisicoesCompraRefinadas();
		for (RequisicaoCompraRefinada req : reqRefinadas) {
			centros.add(req.getRequisicaoCompra().getCentroFinanceiro());
		}
		return centros.toArray(new CentroFinanceiro[]{});
	}

	@Transient
	public BigDecimal getTotalQtdRateio() {
		BigDecimal total = BigDecimal.ZERO;
		for (RateioItemRecebimento rir : this.getRateios()) {
			total = total.setScale(4, RoundingMode.HALF_EVEN).add(rir.getQuantidade().setScale(4, RoundingMode.HALF_EVEN));
		}

		return total;
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
		if (!(ItemRecebimento.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		ItemRecebimento other = (ItemRecebimento) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

}

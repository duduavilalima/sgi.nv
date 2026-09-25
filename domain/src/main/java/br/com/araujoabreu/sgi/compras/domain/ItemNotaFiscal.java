package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro.TipoCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ITEM_NOTA_FISCAL")
@SequenceGenerator(name="SEQ_ITEM_NOTA_FISCAL", sequenceName="SEQ_ITEM_NOTA_FISCAL", allocationSize=1)
public class ItemNotaFiscal extends AuditEntity implements Detail {

	private static final long serialVersionUID = 6165551342343428093L;
	private NotaFiscal notaFiscal;
	private ItemCompra itemCompra;
	private String descricao;
	private BigDecimal quantidade = BigDecimal.valueOf(0L);
	private String quantidadeStr;
	private String marca;
	private BigDecimal preco;
	private String precoStr;
	private List<RateioNotaFiscal> rateios = new ArrayList<>();
	private ContaContabil contaContabilCentroLucro = new ContaContabil();
	private ContaContabil contaContabilCentroCusto = new ContaContabil();

	//Transient
	private String descricaoContaContabilCentroLucro;
	private String descricaoContaContabilCentroCusto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PAGAMENTO", nullable = false)
	@NotNull
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ITEM_COMPRA")
	public ItemCompra getItemCompra() {
		return this.itemCompra;
	}

	public void setItemCompra(ItemCompra itemCompra) {
		this.itemCompra = itemCompra;
	}

	@Column(name = "DESCRICAO")
	public String getDescricao() {
		if (this.descricao != null && !this.descricao.isEmpty())
			return this.descricao.toUpperCase();
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "QUANTIDADE", nullable = false, scale = 6, precision = 15)
	@NotNull
	public BigDecimal getQuantidade() {
		return this.quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	@Column(name = "MARCA")
	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	@Column(name = "PRECO", nullable = false, precision = 12, scale = 4)
	@NotNull
	public BigDecimal getPreco() {
		return this.preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	@Transient
	public BigDecimal getValor() {
		return getPreco().multiply(quantidade);
	}

	@Override
	@Transient
	public Boolean getPrincipal() {
		return false;
	}

	@Override
	public void setPrincipal(Boolean principal) {
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "itemNotaFiscal")
	public List<RateioNotaFiscal> getRateios() {
		return rateios;
	}

	public void setRateios(List<RateioNotaFiscal> rateios) {
		this.rateios = rateios;
	}

	@Transient
	public BigDecimal getValorTotal() {
		BigDecimal valorTotal = new BigDecimal("0");

		if (preco != null && quantidade != null && !preco.equals(BigDecimal.ZERO) && !quantidade.equals(BigDecimal.ZERO)) {
			valorTotal = calcularValorTotal(quantidade, preco);
		}
		return valorTotal;
	}

	private BigDecimal calcularValorTotal(BigDecimal quantidade, BigDecimal preco) {
		BigDecimal valorTotal = new BigDecimal("0");
		if (quantidade != null && preco != null) {
			valorTotal = quantidade.multiply(preco);
			valorTotal = valorTotal.setScale(2, RoundingMode.HALF_UP);
		}
		return valorTotal;
	}

	@Transient
	public String getPrecoStr() {
		return precoStr;
	}

	public void setPrecoStr(String precoStr) {
		this.precoStr = precoStr;
		try {
			precoStr = precoStr.replace(".", "");
			precoStr = precoStr.replace(",", ".");
			preco = new BigDecimal(precoStr);
		} catch (Exception e) {
			preco = new BigDecimal(0);
		}
		preco = preco.setScale(4);
	}

	@Transient
	public String getQuantidadeStr() {
		return quantidadeStr;
	}

	public void setQuantidadeStr(String quantidadeStr) {
		this.quantidadeStr = quantidadeStr;
		quantidadeStr = quantidadeStr.replace(".", "");
		quantidade = new BigDecimal(quantidadeStr.replace(",", ".").replace("_", ""));
		quantidade = quantidade.setScale(itemCompra.getUnidadeMedida().getNumCasasDecimais());
	}

	@Transient
	public String getQuantidadeFormatada() {

		String quantidade = "0,0000";
		if (!StringUtils.isEmpty(this.getQuantidadeStr()) && quantidade != null) {
			BigDecimal quantidadeFormatada = BigDecimal.valueOf(this.quantidade.doubleValue());
			quantidadeFormatada = this.quantidade;
			quantidade = quantidadeFormatada.setScale(4).toString();
			quantidade = quantidade.toString().replace(".", ",");
		}
		return quantidade;
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

	@Transient
	public boolean isContaContabilCentroCustoPermitido() {
		for (RateioNotaFiscal rateioNotaFiscal : rateios) {
			if (rateioNotaFiscal.getCentroFinanceiro() != null && rateioNotaFiscal.getCentroFinanceiro().getPai() == null) {
				return true;
			}
		}
		return false;
	}

	@Transient
	public boolean isContaContabilCentroLucroPermitido() {
		for (RateioNotaFiscal rateioNotaFiscal : rateios) {
			if (rateioNotaFiscal.getCentroFinanceiro() != null && rateioNotaFiscal.getCentroFinanceiro().getPai() != null) {
				return true;
			}
		}
		return false;
	}

	@Transient
	public String getDescricaoContaContabilCentroLucro() {
		return descricaoContaContabilCentroLucro;
	}

	public void setDescricaoContaContabilCentroLucro(String descricaoContaContabilCentroLucro) {
		this.descricaoContaContabilCentroLucro = descricaoContaContabilCentroLucro;
	}

	@Transient
	public String getDescricaoContaContabilCentroCusto() {
		return descricaoContaContabilCentroCusto;
	}

	public void setDescricaoContaContabilCentroCusto(String descricaoContaContabilCentroCusto) {
		this.descricaoContaContabilCentroCusto = descricaoContaContabilCentroCusto;
	}

	@Transient
	public String getCentrosFinanceirosAbreviados() {
		StringBuffer sbCentroFinanceiro = new StringBuffer();
		String centrosFinanceiros = "";
		if (rateios != null && !rateios.isEmpty()) {
			for (RateioNotaFiscal rateio : rateios) {
				sbCentroFinanceiro.append("<b>").append(rateio.getCentroFinanceiro().getIdentificadorFinal()).append("</b>");
				sbCentroFinanceiro.append(" (").append(rateio.getCentroFinanceiro().getNome()).append(") / ");
			}
			centrosFinanceiros = sbCentroFinanceiro.toString().trim();
			centrosFinanceiros = centrosFinanceiros.substring(0, centrosFinanceiros.length() - 1);
			sbCentroFinanceiro = new StringBuffer();
			sbCentroFinanceiro.append(centrosFinanceiros);
		}
		return sbCentroFinanceiro.toString();
	}

	@Transient
	public String getIdentificadorCentroFinanceiro() {
		if (rateios != null && !rateios.isEmpty()) {
			if (rateios.size() == 1)
				return rateios.get(0).getCentroFinanceiro().getIdentificadorFinal();
			else
				return "Vários";
		}
		return null;
	}

	@Transient
	public CentroFinanceiro getCentroFinanceiroRateio() {
		if (rateios != null && !rateios.isEmpty()) {
			if (rateios.size() == 1)
				return rateios.get(0).getCentroFinanceiro();
		}
		return null;
	}

	@Transient
	public String getDescricaoCentroFinanceiro() {
		if (rateios != null && !rateios.isEmpty()) {
			if (rateios.size() == 1)
				return rateios.get(0).getCentroFinanceiro().getIdentificacaoFinal();
			else
				return "Rateio";
		}
		return null;
	}

	@Transient
	public BigDecimal getTotalPorTipoCentroFinanceiro(TipoCentroFinanceiro tipoCentroFinanceiro) {
		BigDecimal total = BigDecimal.ZERO;
		for (RateioNotaFiscal rateio : this.rateios) {
			if (rateio.getCentroFinanceiro().getTipo().equals(tipoCentroFinanceiro))
				total = total.add(rateio.getValor());
		}
		return total;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((itemCompra == null) ? 0 : itemCompra.hashCode());
		result = prime * result + ((marca == null) ? 0 : marca.hashCode());
		result = prime * result + ((notaFiscal == null) ? 0 : notaFiscal.hashCode());
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
		if (!(ItemNotaFiscal.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		ItemNotaFiscal other = (ItemNotaFiscal) obj;
		if (getCodigo() == null || other.getCodigo() == null)
			return false;

		if (getCodigo().equals(other.getCodigo()))
			return true;

		if (this.getItemCompra() == null || other.getItemCompra() == null)
			return false;

		if (this.getNotaFiscal() == null || other.getNotaFiscal() == null)
			return false;

		if (this.getItemCompra().getCodigo().equals(other.getItemCompra().getCodigo())
			&& this.getDescricao().toLowerCase().equals(other.getDescricao().toLowerCase())
			&& this.getMarca().toLowerCase().equals(other.getMarca().toLowerCase())
			&& this.getNotaFiscal().getCodigo().equals(other.getNotaFiscal().getCodigo())
		)
			return true;

		return false;
	}

	@Transient
	public UnidadeMedida getUnidadeMedidaItemCompra() {
		if (itemCompra != null && itemCompra.getCodigo() != null) {
			return itemCompra.getUnidadeMedida();
		}
		return new UnidadeMedida();
	}

	@Override
	public String toString() {
		return "ItemNotaFiscal [" + (getCodigo() != null ? "codigo=" + getCodigo() + ", " : "") + (descricao != null ? "descricao=" + descricao : "") + (marca != null ? "marca=" + marca : "") + "]";
	}

	@Transient
	public String mensagemAuditoriaNF() {
		return this.toString() + " :: Item compra : " + this.getItemCompra().getCodigo() + " - Item compra nome : " + this.getItemCompra().getNome();
	}
}

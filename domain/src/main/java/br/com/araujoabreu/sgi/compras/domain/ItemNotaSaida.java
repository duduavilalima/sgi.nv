package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Entity
@Table(name = "ITEM_NOTA_SAIDA")
@SequenceGenerator(name = "SEQ_ITEM_NOTA_SAIDA", sequenceName = "SEQ_ITEM_NOTA_SAIDA", allocationSize = 1)
public class ItemNotaSaida extends AuditEntity {

	private static final long serialVersionUID = -145425051008460908L;
	private NotaSaida notaSaida;
	private ItemNotaFiscal itemNotaFiscal;
	private ItemRecebimento itemRecebimento;
	private Ncm ncm;
	private Cfop cfop;
	private BigDecimal quantidadeDevolvida;
	private Integer situacaoTributariaIPI;
	private Integer classeEnquadramentoIPI;
	private Long codigoEnquadramentoIPI;
	private String cnpjProdutorIPI;
	private Integer codigoSeloControleIPI;
	private Long quantidadeSeloControleIPI;
	private BigDecimal baseCalculoIPI;
	private BigDecimal aliquotaIPI;
	private SituacoesTributariasIcms situacaoTributariaICMS;
	private Integer modalidadeBaseCalculsoICMS;
	private BigDecimal baseCalculoICMS;
	private BigDecimal aliquotaICMS;

	// Usados na edição e exibição dos itens.
	private boolean selected = false;
	private BigDecimal quantidadeDevolvidaOriginal;

	private Long codigoCfop;
	private Integer codigoNcm;

	public ItemNotaSaida() {

	}

	public ItemNotaSaida(Long codigo) {
		setCodigo(codigo);
	}

	public ItemNotaSaida(Long codigo, NotaSaida notaSaida) {
		setCodigo(codigo);
		this.notaSaida = notaSaida;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA_SAIDA", nullable = false)
	@NotNull
	public NotaSaida getNotaSaida() {
		return notaSaida;
	}

	public void setNotaSaida(NotaSaida notaSaida) {
		this.notaSaida = notaSaida;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ITEM_NF")
	public ItemNotaFiscal getItemNotaFiscal() {
		return itemNotaFiscal;
	}

	public void setItemNotaFiscal(ItemNotaFiscal itemNotaFiscal) {
		this.itemNotaFiscal = itemNotaFiscal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ITEM_RECEBIMENTO")
	public ItemRecebimento getItemRecebimento() {
		return itemRecebimento;
	}

	public void setItemRecebimento(ItemRecebimento itemRecebimento) {
		this.itemRecebimento = itemRecebimento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NCM")
	public Ncm getNcm() {
		return ncm;
	}

	public void setNcm(Ncm ncm) {
		this.ncm = ncm;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CFOP")
	public Cfop getCfop() {
		return cfop;
	}

	public void setCfop(Cfop cfop) {
		this.cfop = cfop;
	}

	@Column(name = "QUANTIDADE_DEVOLVIDA", nullable = false, scale = 4, precision = 15)
	@NotNull
	public BigDecimal getQuantidadeDevolvida() {
		if (quantidadeDevolvida != null) {
			return quantidadeDevolvida.setScale(getUnidadeCasasDecimais(), RoundingMode.HALF_UP);
		}
		return quantidadeDevolvida;
	}

	public void setQuantidadeDevolvida(BigDecimal quantidadeDevolvida) {
		this.quantidadeDevolvida = quantidadeDevolvida;
	}

	@Column(name = "SITUACAO_TRIBUTARIA_IPI")
	public Integer getSituacaoTributariaIPI() {
		return situacaoTributariaIPI;
	}

	public void setSituacaoTributariaIPI(Integer situacaoTributariaIPI) {
		this.situacaoTributariaIPI = situacaoTributariaIPI;
	}

	@Column(name = "CLASSE_ENQUADRAMENTO_IPI")
	public Integer getClasseEnquadramentoIPI() {
		return classeEnquadramentoIPI;
	}

	public void setClasseEnquadramentoIPI(Integer classeEnquadramentoIPI) {
		this.classeEnquadramentoIPI = classeEnquadramentoIPI;
	}

	@Column(name = "CODIGO_ENQUANDRAMENTO_IPI")
	public Long getCodigoEnquadramentoIPI() {
		return codigoEnquadramentoIPI;
	}

	public void setCodigoEnquadramentoIPI(Long codigoEnquadramentoIPI) {
		this.codigoEnquadramentoIPI = codigoEnquadramentoIPI;
	}

	@Column(name = "CNPJ_PRODUTOR_IPI")
	public String getCnpjProdutorIPI() {
		return cnpjProdutorIPI;
	}

	public void setCnpjProdutorIPI(String cnpjProdutorIPI) {
		this.cnpjProdutorIPI = cnpjProdutorIPI;
	}

	@Column(name = "CODIGO_SELO_CONTROLE_IPI")
	public Integer getCodigoSeloControleIPI() {
		return codigoSeloControleIPI;
	}

	public void setCodigoSeloControleIPI(Integer codigoSeloControleIPI) {
		this.codigoSeloControleIPI = codigoSeloControleIPI;
	}

	@Column(name = "QUANTIDADE_SELO_CONTROLE_IPI")
	public Long getQuantidadeSeloControleIPI() {
		return quantidadeSeloControleIPI;
	}

	public void setQuantidadeSeloControleIPI(Long quantidadeSeloControleIPI) {
		this.quantidadeSeloControleIPI = quantidadeSeloControleIPI;
	}

	@Column(name = "BASE_CALCULO_IPI", scale = 2, precision = 15)
	public BigDecimal getBaseCalculoIPI() {
		return baseCalculoIPI;
	}

	public void setBaseCalculoIPI(BigDecimal baseCalculoIPI) {
		this.baseCalculoIPI = baseCalculoIPI;
	}

	@Column(name = "ALIQUOTA_IPI", scale = 2, precision = 4)
	public BigDecimal getAliquotaIPI() {
		return aliquotaIPI;
	}

	public void setAliquotaIPI(BigDecimal aliquotaIPI) {
		this.aliquotaIPI = aliquotaIPI;
	}

	@Column(name = "SITUACAO_TRIBUTARIA_ICMS")
	@Enumerated(EnumType.ORDINAL)
	public SituacoesTributariasIcms getSituacaoTributariaICMS() {
		return situacaoTributariaICMS;
	}

	public void setSituacaoTributariaICMS(SituacoesTributariasIcms situacaoTributariaICMS) {
		this.situacaoTributariaICMS = situacaoTributariaICMS;
	}

	@Column(name = "MODALIDADE_BASE_CALCULO_ICMS")
	public Integer getModalidadeBaseCalculsoICMS() {
		return modalidadeBaseCalculsoICMS;
	}

	public void setModalidadeBaseCalculsoICMS(Integer modalidadeBaseCalculsoICMS) {
		this.modalidadeBaseCalculsoICMS = modalidadeBaseCalculsoICMS;
	}

	@Column(name = "BASE_CALCULO_ICMS", scale = 2, precision = 15)
	public BigDecimal getBaseCalculoICMS() {
		return baseCalculoICMS;
	}

	public void setBaseCalculoICMS(BigDecimal baseCalculoICMS) {
		this.baseCalculoICMS = baseCalculoICMS;
	}

	@Column(name = "ALIQUOTA_ICMS", scale = 2, precision = 4)
	public BigDecimal getAliquotaICMS() {
		return aliquotaICMS;
	}

	public void setAliquotaICMS(BigDecimal aliquotaICMS) {
		this.aliquotaICMS = aliquotaICMS;
	}

	@Transient
	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Transient
	public Long getCodigoCfop() {
		return codigoCfop;
	}

	public void setCodigoCfop(Long codigoCfop) {
		this.codigoCfop = codigoCfop;
	}

	@Transient
	public Integer getCodigoNcm() {
		return codigoNcm;
	}

	public void setCodigoNcm(Integer codigoNcm) {
		this.codigoNcm = codigoNcm;
	}

	@Transient
	public Long getCodigoItem() {
		if (itemNotaFiscal != null) {
			return itemNotaFiscal.getCodigo();
		} else if (itemRecebimento != null) {
			return itemRecebimento.getCodigo();
		}
		return null;
	}


	// ====================== MÉTODOS USADOS NA EDIÇÃO E EXIBIÇÃO DO ITEM ======================

	public void setCodigoItem(Long value) {

	}

	@Transient
	public String getDescricao() {
		if (itemNotaFiscal != null) {
			return itemNotaFiscal.getDescricao();
		} else if (itemRecebimento != null) {
			return itemRecebimento.getItemPedido().getItemProposta().getDescricao();
		}
		return null;
	}

	public void setDescricao(String value) {

	}

	@Transient
	public String getUnidade() {
		if (itemNotaFiscal != null) {
			return itemNotaFiscal.getItemCompra().getUnidadeMedida().getNome();
		} else if (itemRecebimento != null) {
			return itemRecebimento.getItemPedido().getItemProposta().getItemCompra().getUnidadeMedida().getNome();
		}
		return null;
	}

	public void setUnidade(String value) {

	}

	@Transient
	public Integer getUnidadeCasasDecimais() {
		if (itemNotaFiscal != null) {
			return itemNotaFiscal.getItemCompra().getUnidadeMedida().getNumCasasDecimais();
		} else if (itemRecebimento != null) {
			return itemRecebimento.getItemPedido().getItemProposta().getItemCompra().getUnidadeMedida().getNumCasasDecimais();
		}
		return null;
	}

	public void setUnidadeCasasDecimais(Integer value) {

	}

	@Transient
	public BigDecimal getValorUnidade() {
		if (itemNotaFiscal != null) {
			return itemNotaFiscal.getPreco().setScale(4, RoundingMode.HALF_UP);
		} else if (itemRecebimento != null) {
			return itemRecebimento.getItemPedido().getItemProposta().getPreco().setScale(4, RoundingMode.HALF_UP);
		}
		return null;
	}

	public void setValorUnidade(BigDecimal value) {

	}

	@Transient
	public BigDecimal getValorTotal() {
		if (itemNotaFiscal != null) {
			return quantidadeDevolvida.multiply(itemNotaFiscal.getPreco()).setScale(2, RoundingMode.HALF_UP);
		} else if (itemRecebimento != null) {
			BigDecimal total = new BigDecimal(0);
			if (itemRecebimento.getItemPedido().getQuantidadeRecebida() != null && itemRecebimento.getItemPedido().getItemProposta().getValor() != null) {
				total = quantidadeDevolvida.multiply(itemRecebimento.getItemPedido().getItemProposta().getPreco());
				total = total.setScale(2, RoundingMode.HALF_UP);
			}
			return total;
		}
		return null;
	}

	@Transient
	public Double getValorDoIpi() {
		if (this.aliquotaIPI != null && this.baseCalculoIPI != null) {
			return baseCalculoIPI.multiply(aliquotaIPI).doubleValue() / 100;
		}
		return null;
	}

	public void setValorDoIpi(Double value) {

	}

	@Transient
	public Double getValorDoIcms() {
		if (this.aliquotaICMS != null && this.baseCalculoICMS != null) {
			return baseCalculoICMS.multiply(aliquotaICMS).doubleValue() / 100;
		}
		return null;
	}

	public void setValorDoIcms(Double value) {

	}

	@Transient
	public BigDecimal getQuantidadeDevolvidaOriginal() {
		return quantidadeDevolvidaOriginal;
	}

	public void setQuantidadeDevolvidaOriginal(BigDecimal quantidadeDevolvidaOriginal) {
		this.quantidadeDevolvidaOriginal = quantidadeDevolvidaOriginal;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ItemNotaSaida)) return false;
		if (!super.equals(o)) return false;
		ItemNotaSaida that = (ItemNotaSaida) o;
		return getSelected() == that.getSelected() &&
			Objects.equals(getQuantidadeDevolvida(), that.getQuantidadeDevolvida()) &&
			Objects.equals(getSituacaoTributariaIPI(), that.getSituacaoTributariaIPI()) &&
			Objects.equals(getClasseEnquadramentoIPI(), that.getClasseEnquadramentoIPI()) &&
			Objects.equals(getCodigoEnquadramentoIPI(), that.getCodigoEnquadramentoIPI()) &&
			Objects.equals(getCnpjProdutorIPI(), that.getCnpjProdutorIPI()) &&
			Objects.equals(getCodigoSeloControleIPI(), that.getCodigoSeloControleIPI()) &&
			Objects.equals(getQuantidadeSeloControleIPI(), that.getQuantidadeSeloControleIPI()) &&
			Objects.equals(getBaseCalculoIPI(), that.getBaseCalculoIPI()) &&
			Objects.equals(getAliquotaIPI(), that.getAliquotaIPI()) &&
			Objects.equals(getModalidadeBaseCalculsoICMS(), that.getModalidadeBaseCalculsoICMS()) &&
			Objects.equals(getBaseCalculoICMS(), that.getBaseCalculoICMS()) &&
			Objects.equals(getAliquotaICMS(), that.getAliquotaICMS()) &&
			Objects.equals(getQuantidadeDevolvidaOriginal(), that.getQuantidadeDevolvidaOriginal()) &&
			Objects.equals(getCodigoCfop(), that.getCodigoCfop()) &&
			Objects.equals(getCodigoNcm(), that.getCodigoNcm());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getQuantidadeDevolvida(), getSituacaoTributariaIPI(), getClasseEnquadramentoIPI(), getCodigoEnquadramentoIPI(), getCnpjProdutorIPI(), getCodigoSeloControleIPI(), getQuantidadeSeloControleIPI(), getBaseCalculoIPI(), getAliquotaIPI(), getModalidadeBaseCalculsoICMS(), getBaseCalculoICMS(), getAliquotaICMS(), getSelected(), getQuantidadeDevolvidaOriginal(), getCodigoCfop(), getCodigoNcm());
	}

	public enum SituacoesTributariasIcms {

		TRIBUTADA_INTEGRALMENTE("00 - Tributada integralmente", 0),
		ISENTA("40 - Isenta", 40);

		private final String descricao;
		private final int valor;

		private SituacoesTributariasIcms(String descricao, int valor) {
			this.descricao = descricao;
			this.valor = valor;
		}

		public String getDescricao() {
			return descricao;
		}

		public int getValor() {
			return valor;
		}

	}
}

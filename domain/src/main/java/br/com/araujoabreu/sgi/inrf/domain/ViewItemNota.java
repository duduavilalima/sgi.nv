package br.com.araujoabreu.sgi.inrf.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "VW_INRF_ITENS_NOTA_FISCAL")
public class ViewItemNota implements Serializable {

	private static final long serialVersionUID = -3513046045885836692L;

	@EmbeddedId
	private ViewItemNotaCompositeKey id;

	@Column(name = "cod_item")
	private Long codItem;

	@Column(name = "descr_compl")
	private String descrCompl;

	@Column(name = "unidade_medida")
	private String unidadeMedida;

	@Column(name = "codigo_barra")
	private String codigoBarra;

	@Column(name = "codigo_tipo_item_compra")
	private String tipoItemCompra;

	@Column(name = "codigo_subtipo_servico")
	private String subTipoServico;

	@Column(name = "vl_item")
	private BigDecimal vlItem;

	@Column(name = "qtd")
	private BigDecimal qtd;

	@Column(name = "vl_desc")
	private BigDecimal vlDesc;

	@Column(name = "nat_bc_cred")
	private String natBcCred;

	@Column(name = "ind_orig_cred")
	private String indOrigCred;

	@Column(name = "cst_pis")
	private String cstPis;

	@Column(name = "vl_bc_pis")
	private BigDecimal vlBcPis;

	@Column(name = "aliq_pis")
	private BigDecimal aliqPis;

	@Column(name = "cst_cofins")
	private String cstCofins;

	@Column(name = "vl_bc_cofins")
	private BigDecimal vlBcCofins;

	@Column(name = "aliq_cofins")
	private BigDecimal aliqCofins;

	@Column(name = "cod_cta")
	private String codCta;

	@Column(name = "cod_ccus")
	private String codCcus;

	@Column(name = "cf_cumulativo")
	private Boolean cfCumulativo;

	@Column(name = "tipo_cf")
	private CentroFinanceiro.TipoCentroFinanceiro tipoCf;

	public ViewItemNota() {
	}

	public String getTipoItemCompraFormatado() {
		return this.tipoItemCompra;
	}

	public ViewItemNotaCompositeKey getId() {
		return id;
	}

	public void setId(ViewItemNotaCompositeKey id) {
		this.id = id;
	}

	public String getDescrCompl() {
		return descrCompl;
	}

	public void setDescrCompl(String descrCompl) {
		this.descrCompl = descrCompl;
	}

	public BigDecimal getVlItem() {
		return vlItem;
	}

	public void setVlItem(BigDecimal vlItem) {
		this.vlItem = vlItem;
	}

	public BigDecimal getVlDesc() {
		return vlDesc;
	}

	public void setVlDesc(BigDecimal vlDesc) {
		this.vlDesc = vlDesc;
	}

	public String getNatBcCred() {
		return natBcCred;
	}

	public void setNatBcCred(String natBcCred) {
		this.natBcCred = natBcCred;
	}

	public String getIndOrigCred() {
		return indOrigCred;
	}

	public void setIndOrigCred(String indOrigCred) {
		this.indOrigCred = indOrigCred;
	}

	public String getCstPis() {
		return cstPis;
	}

	public void setCstPis(String cstPis) {
		this.cstPis = cstPis;
	}

	public BigDecimal getVlBcPis() {
		return vlBcPis;
	}

	public void setVlBcPis(BigDecimal vlBcPis) {
		this.vlBcPis = vlBcPis;
	}

	public BigDecimal getAliqPis() {
		return aliqPis;
	}

	public void setAliqPis(BigDecimal aliqPis) {
		this.aliqPis = aliqPis;
	}

	public String getCstCofins() {
		return cstCofins;
	}

	public void setCstCofins(String cstCofins) {
		this.cstCofins = cstCofins;
	}

	public BigDecimal getVlBcCofins() {
		return vlBcCofins;
	}

	public void setVlBcCofins(BigDecimal vlBcCofins) {
		this.vlBcCofins = vlBcCofins;
	}

	public BigDecimal getAliqCofins() {
		return aliqCofins;
	}

	public void setAliqCofins(BigDecimal aliqCofins) {
		this.aliqCofins = aliqCofins;
	}

	public String getCodCta() {
		return codCta;
	}

	public void setCodCta(String codCta) {
		this.codCta = codCta;
	}

	public String getCodCcus() {
		return codCcus;
	}

	public void setCodCcus(String codCcus) {
		this.codCcus = codCcus;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getTipoItemCompra() {
		return tipoItemCompra;
	}

	public void setTipoItemCompra(String tipoItemCompra) {
		this.tipoItemCompra = tipoItemCompra;
	}

	public String getSubTipoServico() {
		return subTipoServico;
	}

	public void setSubTipoServico(String subTipoServico) {
		this.subTipoServico = subTipoServico;
	}

	public double getVlCofins() {
		if (this.getAliqCofinsPorCST().equals(BigDecimal.ZERO) || this.getVlBcCofinsPorCST().equals(BigDecimal.ZERO)) {
			return 0D;
		}
		return (this.getAliqCofinsPorCST().doubleValue() * this.getVlBcCofinsPorCST().doubleValue()) / 100;
	}

	public double getVlPis() {
		return (this.getAliqPisPorCST().doubleValue() * this.getVlBcPisPorCST().doubleValue()) / 100;
	}

	public Long getCodItem() {
		return codItem;
	}

	public void setCodItem(Long codItem) {
		this.codItem = codItem;
	}

	public Boolean getCfCumulativo() {
		return cfCumulativo;
	}

	public void setCfCumulativo(Boolean cfCumulativo) {
		this.cfCumulativo = cfCumulativo;
	}

	public CentroFinanceiro.TipoCentroFinanceiro getTipoCf() {
		return tipoCf;
	}

	public void setTipoCf(CentroFinanceiro.TipoCentroFinanceiro tipoCf) {
		this.tipoCf = tipoCf;
	}

	public BigDecimal getQtd() {
		return qtd;
	}

	public void setQtd(BigDecimal qtd) {
		this.qtd = qtd;
	}


	public BigDecimal getVlBcPisPorCST() {
		if (this.cstPis.equals("50")) {
			return vlBcPis;
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal getVlBcCofinsPorCST() {
		if (this.cstCofins.equals("50")) {
			return vlBcCofins;
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal getAliqPisPorCST() {
		if (this.cstPis.equals("50")) {
			return aliqPis;
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal getAliqCofinsPorCST() {
		if (this.cstCofins.equals("50")) {
			return aliqCofins;
		}
		return BigDecimal.ZERO;
	}

}

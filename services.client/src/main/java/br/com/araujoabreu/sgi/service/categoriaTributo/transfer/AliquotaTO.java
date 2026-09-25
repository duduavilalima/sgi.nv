package br.com.araujoabreu.sgi.service.categoriaTributo.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;

public class AliquotaTO extends BaseTO {

	private BigDecimal percentual;
	private BigDecimal limiteInferior;
	private BigDecimal limiteSuperior;
	private Boolean principal;
	private Long codigoCategoriaTributo;
	private Long codigoTributo;
	private String nomeTributo;
	private CategoriaTributoTO categoriaTributo;

	public AliquotaTO() {
	}

	public AliquotaTO(long codigo) {
		setCodigo(codigo);
	}

	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	public BigDecimal getLimiteInferior() {
		return limiteInferior;
	}

	public void setLimiteInferior(BigDecimal limiteInferior) {
		this.limiteInferior = limiteInferior;
	}

	public BigDecimal getLimiteSuperior() {
		return limiteSuperior;
	}

	public void setLimiteSuperior(BigDecimal limiteSuperior) {
		this.limiteSuperior = limiteSuperior;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public Long getCodigoCategoriaTributo() {
		return codigoCategoriaTributo;
	}

	public void setCodigoCategoriaTributo(Long codigoCategoriaTributo) {
		this.codigoCategoriaTributo = codigoCategoriaTributo;
	}

	public Long getCodigoTributo() {
		return codigoTributo;
	}

	public void setCodigoTributo(Long codigoTributo) {
		this.codigoTributo = codigoTributo;
	}

	public String getNomeTributo() {
		return nomeTributo;
	}

	public void setNomeTributo(String nomeTributo) {
		this.nomeTributo = nomeTributo;
	}

	public CategoriaTributoTO getCategoriaTributo() {
		return categoriaTributo;
	}

	public void setCategoriaTributo(CategoriaTributoTO categoriaTributo) {
		this.categoriaTributo = categoriaTributo;
	}
}

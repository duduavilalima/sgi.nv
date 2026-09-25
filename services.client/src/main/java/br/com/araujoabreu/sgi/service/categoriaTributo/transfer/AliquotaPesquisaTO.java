package br.com.araujoabreu.sgi.service.categoriaTributo.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;

public class AliquotaPesquisaTO extends BaseSearchTO {

	private BigDecimal percentual;
	private BigDecimal limiteInferior;
	private BigDecimal limiteSuperior;
	private Boolean principal;
	private Long codigoCategoriaTributo;

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
}

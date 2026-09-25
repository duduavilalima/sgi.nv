package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.AliquotaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;

public class NotaTributoAliquotaEmissaoTO extends BaseTO {

	private BigDecimal baseCalculo;
	private BigDecimal percentual;
	private BigDecimal valor;
	private AliquotaTO aliquota;

	public BigDecimal getBaseCalculo() {return baseCalculo;	}

	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public AliquotaTO getAliquota() {
		return aliquota;
	}

	public void setAliquota(AliquotaTO aliquota) {
		this.aliquota = aliquota;
	}
}

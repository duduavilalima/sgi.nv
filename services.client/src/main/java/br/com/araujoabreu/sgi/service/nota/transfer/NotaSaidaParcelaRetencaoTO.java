package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;

public class NotaSaidaParcelaRetencaoTO extends BaseTO {
	private NotaSaidaParcelaTO parcela;
	private String tipo;
	private BigDecimal valorAReter = BigDecimal.ZERO;
	private BigDecimal valorRetido = BigDecimal.ZERO;

	public NotaSaidaParcelaTO getParcela() {
		return parcela;
	}

	public void setParcela(NotaSaidaParcelaTO parcela) {
		this.parcela = parcela;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getValorAReter() {
		return valorAReter;
	}

	public void setValorAReter(BigDecimal valorAReter) {
		this.valorAReter = valorAReter;
	}

	public BigDecimal getValorRetido() {
		return valorRetido;
	}

	public void setValorRetido(BigDecimal valorRetido) {
		this.valorRetido = valorRetido;
	}
}

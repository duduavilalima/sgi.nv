package br.com.araujoabreu.sgi.tesouraria.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class SecaoRelacaoBancariaPagamentosVO {
	private String titulo;
	private int numeroPagamentos;
	private BigDecimal valorTotalPagamentos;
	private TipoSecao tipo;


	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getNumeroPagamentos() {
		return numeroPagamentos;
	}

	public void setNumeroPagamentos(int numeroPagamentos) {
		this.numeroPagamentos = numeroPagamentos;
	}

	public BigDecimal getValorTotalPagamentos() {
		return valorTotalPagamentos;
	}

	public void setValorTotalPagamentos(BigDecimal valorTotalPagamentos) {
		this.valorTotalPagamentos = valorTotalPagamentos;
	}

	public TipoSecao getTipo() {
		return tipo;
	}

	public void setTipo(TipoSecao tipo) {
		this.tipo = tipo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SecaoRelacaoBancariaPagamentosVO that = (SecaoRelacaoBancariaPagamentosVO) o;
		return numeroPagamentos == that.numeroPagamentos &&
			titulo.equals(that.titulo) &&
			valorTotalPagamentos.equals(that.valorTotalPagamentos);
	}

	@Override
	public int hashCode() {
		return Objects.hash(titulo, numeroPagamentos, valorTotalPagamentos);
	}

	@Override
	public String toString() {
		return "DashboardRelacaoBancariaPagamentosVO [titulo=" + titulo
			+ ", numeroPagamentos=" + numeroPagamentos
			+ ", valorTotalPagamentos=" + valorTotalPagamentos + "]";
	}
}

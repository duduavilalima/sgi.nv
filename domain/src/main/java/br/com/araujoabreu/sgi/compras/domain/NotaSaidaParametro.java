package br.com.araujoabreu.sgi.compras.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Para representar o código e valor definidos no parâmetro.
 *
 * @author danieloliveira
 */
public class NotaSaidaParametro implements Serializable {

	private static final long serialVersionUID = -837613473594588681L;
	private String codigo;
	private String valor;

	public NotaSaidaParametro() {
	}

	public NotaSaidaParametro(String codigo, String valor) {
		this.codigo = codigo;
		this.valor = valor;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NotaSaidaParametro)) return false;
		NotaSaidaParametro that = (NotaSaidaParametro) o;
		return Objects.equals(getCodigo(), that.getCodigo()) &&
			Objects.equals(getValor(), that.getValor());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigo(), getValor());
	}

	@Override
	public String toString() {
		return "NotaSaidaParametro{" +
			"codigo='" + codigo + '\'' +
			", valor='" + valor + '\'' +
			'}';
	}
}

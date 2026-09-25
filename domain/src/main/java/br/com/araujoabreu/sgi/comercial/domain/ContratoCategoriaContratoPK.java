package br.com.araujoabreu.sgi.comercial.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ContratoCategoriaContratoPK implements Serializable {

	private static final long serialVersionUID = -6580665620489652547L;
	@Column(name = "CODIGO_CONTRATO")
	private Long codigoContrato;

	@Column(name = "CODIGO_CATEGORIA_CONTRATO")
	private Long codigoCategoriaContrato;

	public ContratoCategoriaContratoPK() {

	}

	public Long getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(Long codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public Long getCodigoCategoriaContrato() {
		return codigoCategoriaContrato;
	}

	public void setCodigoCategoriaContrato(Long codigoCategoriaContrato) {
		this.codigoCategoriaContrato = codigoCategoriaContrato;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ContratoCategoriaContratoPK that = (ContratoCategoriaContratoPK) o;

		if (!Objects.equals(codigoContrato, that.codigoContrato))
			return false;
		return Objects.equals(codigoCategoriaContrato, that.codigoCategoriaContrato);

	}

	@Override
	public int hashCode() {
		int result = codigoContrato != null ? codigoContrato.hashCode() : 0;
		result = 31 * result + (codigoCategoriaContrato != null ? codigoCategoriaContrato.hashCode() : 0);
		return result;
	}
}

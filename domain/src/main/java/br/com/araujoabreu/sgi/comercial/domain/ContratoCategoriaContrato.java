package br.com.araujoabreu.sgi.comercial.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
// FIXME: Chave composta não funciona no Spring Repository
//@IdClass(ContratoCategoriaContratoPK.class)
@Table(name = "CONTRATO_CATEGORIA_CONTRATO")
public class ContratoCategoriaContrato implements Serializable {

	private static final long serialVersionUID = -7672846967874377284L;
	@Id
	@Column(name = "CODIGO_CONTRATO")
	private Long codigoContrato;

	//FIXME: Chave composta não funciona no Spring Repository
//	@Id
	@Column(name = "CODIGO_CATEGORIA_CONTRATO")
	private Long codigoCategoriaContrato;

	public ContratoCategoriaContrato() {

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
		if (!(o instanceof ContratoCategoriaContrato)) return false;
		ContratoCategoriaContrato that = (ContratoCategoriaContrato) o;
		return Objects.equals(getCodigoContrato(), that.getCodigoContrato()) &&
			Objects.equals(getCodigoCategoriaContrato(), that.getCodigoCategoriaContrato());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigoContrato(), getCodigoCategoriaContrato());
	}
}

package br.com.araujoabreu.sgi.inrf.domain;

import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "VW_INRF_NOTAS_VALIDAS")
public class ViewNotaValida implements Serializable {

	private static final long serialVersionUID = 2982093036736245088L;

	@Id
	@Column(name = "codigo_nota")
	@NotNull
	private Long codigoNota;

	@Column(name = "codigo_pessoa")
	private Long codigoPessoa;

	@Column(name = "data")
	@Temporal(TemporalType.DATE)
	private Date data;

	@Column(name = "codigo_gerencia")
	private Long codigoGerencia;

	@Column(name = "codigo_unidade")
	private Long codigoUnidade;

	@Column(name = "bloco")
	private String bloco;

	public ViewNotaValida() {
	}

	public Long getCodigoNota() {
		return codigoNota;
	}

	public void setCodigoNota(Long codigoNota) {
		this.codigoNota = codigoNota;
	}

	public Long getCodigoPessoa() {
		return codigoPessoa;
	}

	public void setCodigoPessoa(Long codigoPessoa) {
		this.codigoPessoa = codigoPessoa;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Long getCodigoGerencia() {
		return codigoGerencia;
	}

	public void setCodigoGerencia(Long codigoGerencia) {
		this.codigoGerencia = codigoGerencia;
	}

	public Long getCodigoUnidade() {
		return codigoUnidade;
	}

	public void setCodigoUnidade(Long codigoUnidade) {
		this.codigoUnidade = codigoUnidade;
	}

	public String getBloco() {
		return bloco;
	}

	public void setBloco(String bloco) {
		this.bloco = bloco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoNota == null) ? 0 : codigoNota.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(ViewNotaValida.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		ViewNotaValida other = (ViewNotaValida) obj;
		if (codigoNota == null) {
			if (other.getCodigoNota() != null) {
				return false;
			}
		} else if (!codigoNota.equals(other.getCodigoNota())) {
			return false;
		}
		return true;
	}

}

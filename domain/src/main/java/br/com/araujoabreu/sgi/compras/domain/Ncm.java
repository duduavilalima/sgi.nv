package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "NCM")
@SequenceGenerator(name="SEQ_NCM", sequenceName="SEQ_NCM", allocationSize=1)
public class Ncm extends AuditEntity {

	private static final long serialVersionUID = -1439113504880311527L;
	private Integer codigoNcm;
	private String descricao;
	private Ncm pai;

	private Double valor;

	@Transient
	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Column(name = "CODIGO_NCM")
	@NotNull
	public Integer getCodigoNcm() {
		return codigoNcm;
	}

	public void setCodigoNcm(Integer codigoNcm) {
		this.codigoNcm = codigoNcm;
	}

	@Column(name = "DESCRICAO")
	@Length(max = 250)
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PAI")
	public Ncm getPai() {
		return pai;
	}

	public void setPai(Ncm pai) {
		this.pai = pai;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
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
		if (!(Ncm.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Ncm other = (Ncm) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}
}

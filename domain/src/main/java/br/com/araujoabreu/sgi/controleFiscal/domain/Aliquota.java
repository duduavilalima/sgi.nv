package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstração (Super Classe) para tipos de alíquota (Fixa, Variável).
 *
 * @author Rodrigo Barreiros
 */
@Entity
@Table(name = "ALIQUOTA")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name="SEQ_ALIQUOTA", sequenceName="SEQ_ALIQUOTA", allocationSize=1)
public class Aliquota extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 4149187060273107075L;
	private BigDecimal percentual;
	private CategoriaTributo categoriaTributo;
	private BigDecimal limiteInferior;
	private BigDecimal limiteSuperior;
	private Boolean principal;
	private List<AliquotaAgregada> listaAliquotasAgregadas = new ArrayList<>();

	@Column(name = "PERCENTUAL", precision = 9, scale = 2)
	@NotNull
	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CATEGORIA_TRIBUTO")
	@NotNull
	public CategoriaTributo getCategoriaTributo() {
		return categoriaTributo;
	}

	public void setCategoriaTributo(CategoriaTributo categoriaTributo) {
		this.categoriaTributo = categoriaTributo;
	}

	@Column(name = "LIMITE_INFERIOR", precision = 14, scale = 2)
	public BigDecimal getLimiteInferior() {
		return limiteInferior;
	}

	public void setLimiteInferior(BigDecimal limiteInferior) {
		this.limiteInferior = limiteInferior;
	}

	@Column(name = "LIMITE_SUPERIOR", precision = 14, scale = 2)
	public BigDecimal getLimiteSuperior() {
		return limiteSuperior;
	}

	public void setLimiteSuperior(BigDecimal limiteSuperior) {
		this.limiteSuperior = limiteSuperior;
	}

	@Column(name = "PRINCIPAL", nullable = false)
	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "aliquota")
	@Cascade({org.hibernate.annotations.CascadeType.ALL})
	@OrderBy(value = "percentual DESC")
	public List<AliquotaAgregada> getListaAliquotasAgregadas() {
		return listaAliquotasAgregadas;
	}

	public void setListaAliquotasAgregadas(
		List<AliquotaAgregada> listaAliquotasAgregadas) {
		this.listaAliquotasAgregadas = listaAliquotasAgregadas;
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
		if (!(Aliquota.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Aliquota other = (Aliquota) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Aliquota [codigo=" + getCodigo() + ", percentual=" + percentual + ", principal=" + principal + "]";
	}

//	@Transient
//	public String getCidadeUfPercentual(){
//		if (categoriaTributo != null && categoriaTributo.getCidade() != null){
//			return categoriaTributo.getCidade().getNome()+"-"+categoriaTributo.getCidade().getUf()+percentual!=null?" / "+percentual:"";
//		}
//		return "";
//	}

	@Transient
	public String getCidadeUfPercentual() {
		StringBuilder sbCidade = new StringBuilder();
		if (categoriaTributo != null && categoriaTributo.getCidade() != null) {
			sbCidade.append(categoriaTributo.getCidade().getCidadeEstado());
			if (percentual != null) {
				sbCidade.append(" / ");
				sbCidade.append(percentual);
				sbCidade.append(" %");
			}
			return sbCidade.toString();
		}
		return "";
	}

	@Transient
	public String getPercentualCidadeUf() {
		StringBuilder sbCidade = new StringBuilder();
		if (categoriaTributo != null && categoriaTributo.getCidade() != null) {
			if (percentual != null) {
				sbCidade.append(percentual);
				sbCidade.append(" %");
				sbCidade.append(" - ");
			}

			sbCidade.append(categoriaTributo.getCidade().getEstadoCidade());

			return sbCidade.toString();
		}
		return "";
	}
}
